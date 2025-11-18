package infrastructure.db

import java.sql.Connection

object Migrations {

    fun migrate(conn: Connection) {
        conn.createStatement().use { st ->
            st.executeUpdate(
                """
                CREATE TABLE IF NOT EXISTS users (
                    login TEXT PRIMARY KEY,
                    salt TEXT NOT NULL,
                    password_hash TEXT NOT NULL
                );
                """.trimIndent()
            )

            st.executeUpdate(
                """
                CREATE TABLE IF NOT EXISTS resources (
                    path TEXT PRIMARY KEY,
                    max_volume INTEGER NOT NULL
                );
                """.trimIndent()
            )

            st.executeUpdate(
                """
                CREATE TABLE IF NOT EXISTS permissions (
                    user_login TEXT NOT NULL,
                    resource_path TEXT NOT NULL,
                    action TEXT NOT NULL,
                    PRIMARY KEY (user_login, resource_path, action)
                );
                """.trimIndent()
            )
        }

        // Простейший seed (если таблицы пустые)
        conn.createStatement().use { st ->
            st.executeUpdate(
                """
                INSERT OR IGNORE INTO users(login, salt, password_hash) VALUES
                ('alice', 'nan',    'a547590f99d5fd581b3ce00f83757bb2019ec16e54816418e1e932ddbd8afadc'),
                ('bob',   'pepper', '744a9101f7182a6ae0d978121ff74e33cac8d2832579c0637c1c37e9bbb6c065');
                """.trimIndent()
            )

            st.executeUpdate(
                """
                INSERT OR IGNORE INTO resources(path, max_volume) VALUES
                ('A',        100),
                ('A.B',       60),
                ('A.B.C',     30),
                ('A.B.C.f_d', 10),
                ('A.A8B',     80);
                """.trimIndent()
            )

            st.executeUpdate(
                """
                INSERT OR IGNORE INTO permissions(user_login, resource_path, action) VALUES
                ('alice', 'A',   'read'),
                ('alice', 'A.B', 'read'),
                ('alice', 'A.B', 'write'),
                ('paul',  'A.A8B', 'read');
                """.trimIndent()
            )
        }
    }
}