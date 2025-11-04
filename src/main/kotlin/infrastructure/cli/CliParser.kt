package infrastructure.cli

import domain.model.Action
import kotlinx.cli.*
import usecase.CheckAccessInput

sealed interface CliParse {
    data class Ok(val input: CheckAccessInput) : CliParse
    data object Help : CliParse
    data object BadFormat : CliParse
    data object UnknownAction : CliParse
}

class CliParser {
    fun parse(argv: Array<String>): CliParse {
        val parser = ArgParser("app")

        val login     by parser.option(ArgType.String, fullName = "login")
        val password  by parser.option(ArgType.String, fullName = "password")
        val actionStr by parser.option(ArgType.String, fullName = "action")
        val resource  by parser.option(ArgType.String, fullName = "resource")
        val volumeStr by parser.option(ArgType.String, fullName = "volume")
        val help      by parser.option(ArgType.Boolean, shortName = "h", fullName = "help")
            .default(false)

        return try {
            parser.parse(argv)
            if (help) return CliParse.Help

            val action = when (actionStr?.lowercase()) {
                "read" -> Action.read
                "write" -> Action.write
                "exec" -> Action.exec
                else -> return CliParse.UnknownAction
            }
            val v = volumeStr?.toIntOrNull() ?: return CliParse.BadFormat

            val input = CheckAccessInput(
                login = login ?: return CliParse.BadFormat,
                password = password ?: return CliParse.BadFormat,
                action = action,
                resource = resource ?: return CliParse.BadFormat,
                volume = v
            )
            CliParse.Ok(input)
        } catch (_: Exception) {
            CliParse.Help
        }
    }
}