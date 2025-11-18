package infrastructure.db

import domain.model.User
import domain.ports.UserRepository
import java.sql.Connection

class SqliteUserRepository(
    private val conn: Connection
) : UserRepository {

    override fun find(login: String): User? {
        val sql = "SELECT login, salt, password_hash FROM users WHERE login = ?"
        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, login)
            stmt.executeQuery().use { rs ->
                if (!rs.next()) return null
                return User(
                    login = rs.getString("login"),
                    salt = rs.getString("salt"),
                    passwordHashHex = rs.getString("password_hash")
                )
            }
        }
    }
}