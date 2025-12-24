package infrastructure.cli

import domain.model.Action
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import usecase.CheckAccessInput

sealed interface CliParse {
    data class Ok(val input: CheckAccessInput) : CliParse
    data object Help : CliParse
    data object BadFormat : CliParse
    data object UnknownAction : CliParse
}

class CliParser {

    fun parse(argv: Array<String>): CliParse {
        val parser = ArgParser(programName = "app")

        val login by parser.option(ArgType.String, fullName = "login")
        val password by parser.option(ArgType.String, fullName = "password")
        val actionStr by parser.option(ArgType.String, fullName = "action")
        val resource by parser.option(ArgType.String, fullName = "resource")
        val volumeStr by parser.option(ArgType.String, fullName = "volume")
        val help by parser.option(ArgType.Boolean, shortName = "h", fullName = "help")
            .default(false)

        try {
            parser.parse(argv)
        } catch (_: Exception) {
            return CliParse.BadFormat
        }

        if (help) return CliParse.Help

        val l = login ?: return CliParse.BadFormat
        val p = password ?: return CliParse.BadFormat
        val r = resource ?: return CliParse.BadFormat
        val v = volumeStr?.toIntOrNull() ?: return CliParse.BadFormat

        val act = when (actionStr?.lowercase()) {
            "read" -> Action.read
            "write" -> Action.write
            "exec" -> Action.exec
            null -> return CliParse.BadFormat
            else -> return CliParse.UnknownAction
        }

        return CliParse.Ok(
            CheckAccessInput(
                login = l,
                password = p,
                action = act,
                resource = r,
                volume = v
            )
        )
    }
}
