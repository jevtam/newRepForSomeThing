package infrastructure.cli

import kotlin.system.exitProcess
import usecase.CheckAccessResult

object CliPresenter {

    fun showHelpAndExit(): Nothing {
        println(
            """
Usage:
  app --login <name> --password <pwd> --action <read|write|exec> --resource <A.B.C> --volume <int>
  app -h|--help

Exit codes:
  0 OK
  1 Help
  2 Bad login or password
  5 No access
  6 Resource not found
  7 Bad format
  8 Volume exceeded
  9 DB connection error
  10 SQL error
            """.trimIndent()
        )
        exitProcess(1)
    }

    fun exitBadFormat(): Nothing = exitProcess(7)

    fun present(result: CheckAccessResult): Nothing {
        val code = when (result) {
            CheckAccessResult.Ok -> 0
            CheckAccessResult.BadLoginOrPassword -> 2
            CheckAccessResult.NoAccess -> 5
            CheckAccessResult.ResourceNotFound -> 6
            CheckAccessResult.BadFormat -> 7
            CheckAccessResult.VolumeExceeded -> 8
        }
        exitProcess(code)
    }
}
