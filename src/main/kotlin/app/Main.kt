package app

import domain.services.AccessPolicy
import domain.services.AuthService
import domain.services.QuotaService
import infrastructure.cli.CliParser
import infrastructure.cli.CliPresenter
import infrastructure.repo.*
import infrastructure.crypto.Sha256Hasher
import usecase.CheckAccess

fun main(argv: Array<String>) {
    val parse = CliParser().parse(argv)
    if (parse is infrastructure.cli.CliParse.Help) CliPresenter.showHelpAndExit()
    if (parse is infrastructure.cli.CliParse.UnknownAction) CliPresenter.present(usecase.CheckAccessResult.BadFormat)
    if (parse !is infrastructure.cli.CliParse.Ok) CliPresenter.present(usecase.CheckAccessResult.BadFormat)

    val users = InMemoryUserRepository()
    val resources = InMemoryResourceRepository()
    val perms = InMemoryPermissionService()
    val hasher = Sha256Hasher()

    val uc = CheckAccess(
        auth = AuthService(users, hasher),
        policy = AccessPolicy(perms),
        quota = QuotaService(resources)
    )

    val result = uc.execute(parse.input)
    CliPresenter.present(result)
}