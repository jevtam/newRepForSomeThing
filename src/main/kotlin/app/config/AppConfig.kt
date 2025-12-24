package app.config

import infrastructure.crypto.Sha256Hasher
import infrastructure.db.Migrations
import infrastructure.db.SqlitePermissionService
import infrastructure.db.SqliteResourceRepository
import infrastructure.db.SqliteUserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.sql.Connection
import java.sql.DriverManager

@Configuration
class AppConfig {

    @Bean
    fun connection(): Connection {
        val conn = DriverManager.getConnection("jdbc:sqlite:data.db")
        Migrations.migrate(conn)
        return conn
    }

    @Bean fun userRepo(conn: Connection) = SqliteUserRepository(conn)
    @Bean fun resourceRepo(conn: Connection) = SqliteResourceRepository(conn)
    @Bean fun permService(conn: Connection) = SqlitePermissionService(conn)

    @Bean fun hasher() = Sha256Hasher()
}
