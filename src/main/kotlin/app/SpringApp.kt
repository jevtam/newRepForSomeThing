package app

import infrastructure.cli.CliParser
import infrastructure.cli.CliPresenter
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import usecase.CheckAccess

@SpringBootApplication
class SpringApp {

    @Bean
    fun runner(usecase: CheckAccess): CommandLineRunner = CommandLineRunner { args ->
        val parsed = CliParser().parse(args)

        when (parsed) {
            infrastructure.cli.CliParse.Help -> CliPresenter.showHelpAndExit()
            infrastructure.cli.CliParse.BadFormat,
            infrastructure.cli.CliParse.UnknownAction -> CliPresenter.exitBadFormat()
            is infrastructure.cli.CliParse.Ok -> {
                val result = usecase.execute(parsed.input)
                CliPresenter.present(result)
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<SpringApp>(*args)
}
