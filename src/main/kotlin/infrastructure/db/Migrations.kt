package infrastructure.db

import java.sql.Connection

object Migrations {
    fun migrate(conn: Connection) {
        runSqlFile(conn, "scripts/init.sql")
        runSqlFile(conn, "scripts/fill.sql")
    }

    private fun runSqlFile(conn: Connection, path: String) {
        val sql = try {
            java.io.File(path).readText()
        } catch (e: Exception) {
            throw java.sql.SQLException("Cannot read SQL script: $path", e)
        }

        val statements = sql
            .split(";")
            .map { it.trim() }
            .filter { it.isNotBlank() && !it.startsWith("--") }

        conn.createStatement().use { st ->
            for (stmt in statements) st.execute(stmt)
        }
    }
}