package infrastructure.db

import domain.model.Resource
import domain.ports.ResourceRepository
import java.sql.Connection

class SqliteResourceRepository(
    private val conn: Connection
) : ResourceRepository {

    override fun find(path: String): Resource? {
        val sql = "SELECT path, max_volume FROM resources WHERE path = ?"
        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, path)
            stmt.executeQuery().use { rs ->
                if (!rs.next()) return null
                return Resource(
                    path = rs.getString("path"),
                    maxVolume = rs.getInt("max_volume")
                )
            }
        }
    }
}