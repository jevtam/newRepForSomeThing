package app

import domain.services.AccessPolicy
import domain.services.AuthService
import domain.services.QuotaService
import infrastructure.cli.CliParser
import infrastructure.cli.CliPresenter
import infrastructure.crypto.Sha256Hasher
import usecase.CheckAccess
import infrastructure.db.*

fun main(args: Array<String>) {
    val parser = CliParser()
    val input = parser.parse(args)

    db.getConnection().use { conn ->
        Migrations.migrate(conn)

        val userRepo = SqliteUserRepository(conn)
        val resRepo  = SqliteResourceRepository(conn)
        val permSvc  = SqlitePermissionService(conn)
        val hasher   = Sha256Hasher()

        val auth  = AuthService(userRepo, hasher)
        val quota = QuotaService(resRepo)
        val policy = AccessPolicy(permSvc)

        val usecase = CheckAccess(auth, policy, quota)

        val result = usecase.execute(input)
        CliPresenter.present(result)
    }
}