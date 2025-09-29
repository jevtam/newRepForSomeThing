package app

import  kotlin.system.exitProcess
import java.security.MessageDigest
import kotlinx.cli.*

enum class Action {read, write, exec}
data class User (val login: String, val salt: String, val passwordHashHex: String)//данные пользователя, соль и хэш пароля
typealias Permissions = Map<String, Set<Action>>//права
data class Resource(val path: String, val maxVolume: Int)//ресурс для максимального объема

// бд с двумя пользователями
object Db {
    val users: Map<String, User> = listOf(
        User ("alice", "nan", "a547590f99d5fd581b3ce00f83757bb2019ec16e54816418e1e932ddbd8afadc"),
        User ("bob", "pepper", "744a9101f7182a6ae0d978121ff74e33cac8d2832579c0637c1c37e9bbb6c065")
    ).associateBy { it.login }

    //иерархия ресурсов
    val resources: Map<String, Resource> = listOf(
        Resource("A", 100),
        Resource("A.B", 60),
        Resource("A.B.C", 30),
        Resource("A.B.C.f_d", 10),
        Resource("A.A8B", 80)
    ).associateBy { it.path }


    //права пользователей
    val permissions: Map<String, Permissions> = mapOf(
        "alice" to mapOf(
            "A" to setOf(Action.read),
            "A.B" to setOf(Action.read, Action.write)
        ),
        "bob" to mapOf(
            "A.A8B" to setOf(Action.read)
        )
    )
}

// утилиты
fun sha256Hex(s: String): String =
    MessageDigest.getInstance("SHA-256")
        .digest(s.toByteArray(Charsets.UTF_8))
        .joinToString("") { "%02x".format(it) }

fun checkPassword(user: User, password: String): Boolean=
    sha256Hex(user.salt + password)== user.passwordHashHex

private val NAME_RE = Regex("^[A-Za-z0-9_]{1,20}$")

// из A.B.C в A.B.C, A.B, A
fun ancestors(path: String): Sequence<String> = sequence {
    var cur = path
    while (true) {
        yield(cur)
        val idx = cur.lastIndexOf('.')
        if (idx < 0) break
        cur = cur.substring(0, idx)
    }
}

// разрешенные действия для login и path
fun effectivePermissions(login: String, path: String): Set<Action> {
    val userPerms = Db.permissions[login] ?: return emptySet()
    val acc = mutableSetOf<Action>()
    for (p in ancestors(path)) {
        userPerms[p]?.let { acc += it }
    }
    return acc
}

class Args(
    val login: String?,
    val password: String?,
    val actionStr: String?,
    val resource: String?,
    val volumeStr: String?,
    val help: Boolean
)

// парсинг аргументов
fun parseArgs(argv: Array<String>): Args {
    val parser = ArgParser("app")

    val login     by parser.option(ArgType.String,  fullName = "login",    description = "Логин пользователя")
    val password  by parser.option(ArgType.String,  fullName = "password", description = "Пароль (SHA-256(salt+password))")
    val actionStr by parser.option(ArgType.String,  fullName = "action",   description = "Действие: read|write|exec")
    val resource  by parser.option(ArgType.String,  fullName = "resource", description = "Путь к ресурсу A.B.C")
    val volumeStr by parser.option(ArgType.String,  fullName = "volume",   description = "Запрашиваемый объем (целое >= 0)")
    val help      by parser.option(ArgType.Boolean, shortName = "h", fullName = "help",
        description = "Показать справку").default(false)

    try {
        parser.parse(argv)
    } catch (e: Exception) {
        // некорректный формат запуска -> показать help и вернуть как help
        println(e.message ?: "")
        printHelp()
        return Args(null, null, null, null, null, true)
    }

    return Args(login, password, actionStr, resource, volumeStr, help)
}

fun printHelp() {
    println(
        """
Usage:
  java -jar app.jar --login <name> --password <pwd> --action <read|write|exec> --resource <A.B.C> --volume <int>
Options:
  --login      Логин пользователя
  --password   Пароль (SHA-256(salt+password))
  --action     read | write | exec
  --resource   Путь, сегменты [A-Za-z0-9_]{1,20} через '.'
  --volume     Целое число (>= 0)
  -h, --help   Показать справку
Exit codes: 0..8 (см. README)
        """.trimIndent()
    )
}

fun main(argv: Array<String>) {
    val a = parseArgs(argv)

    if (a.help) {
        printHelp()
        exitProcess(1)
    }
    //нормализация
    val login = a.login ?: run { printHelp(); exitProcess(1) }
    val password = a.password ?: run { printHelp(); exitProcess(1) }
    val action: Action = when (a.actionStr?.lowercase()) {
        "read" -> Action.read
        "write" -> Action.write
        "exec" -> Action.exec
        null -> run { printHelp(); exitProcess(1) }
        else -> exitProcess(4) // неизвестное действие
    }
    val resource = a.resource ?: run { printHelp(); exitProcess(1) }
    val volume = a.volumeStr?.toIntOrNull() ?: run { exitProcess(7) }

    // логин пароль
    val user = Db.users[login] ?: exitProcess(3)
    if (!checkPassword(user, password)) exitProcess(2)

    // существование ресурса
    val res = Db.resources[resource] ?: exitProcess(6)

    // доступ
    val perms = effectivePermissions(login, resource)
    if (action !in perms) exitProcess(5)

    // лимит
    if (volume < 0) exitProcess(7)
    if (volume > res.maxVolume) exitProcess(8)

    exitProcess(0)
}
