package infrastructure.db

import java.sql.Connection
import java.sql.DriverManager

object db {
    private const val URL = "jdbc:sqlite:data.db"

    fun getConnection(): Connection = DriverManager.getConnection(URL)
}