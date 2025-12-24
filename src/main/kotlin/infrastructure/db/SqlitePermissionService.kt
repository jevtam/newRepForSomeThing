package infrastructure.db

import domain.model.Action
import domain.ports.PermissionService
import java.sql.Connection

class SqlitePermissionService(
    private val conn: Connection
) : PermissionService {

    override fun allowed(login: String, resourcePath: String): Set<Action> {
        val sql = """
            SELECT action
            FROM permissions
            WHERE user_login = ? AND resource_path = ?
        """.trimIndent()

        val result = mutableSetOf<Action>()
        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, login)
            stmt.setString(2, resourcePath)
            stmt.executeQuery().use { rs ->
                while (rs.next()) {
                    val actionName = rs.getString("action")
                    result += Action.valueOf(actionName) // если enum
                }
            }
        }
        return result
    }
}