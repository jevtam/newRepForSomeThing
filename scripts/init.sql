--внешние ключи
PRAGMA foreign_keys = ON;

--таблица пользователей
CREATE TABLE IF NOT EXISTS users (
    login TEXT PRIMARY KEY,
    salt TEXT NOT NULL,
    password_hash TEXT NOT NULL
);

--таблица ресурсов
CREATE TABLE IF NOT EXISTS resources (
    path TEXT PRIMARY KEY,
    max_volume INTEGER NOT NULL CHECK (max_volume >= 0)
);

--таблица прав
CREATE TABLE IF NOT EXISTS permissions (
    user_login TEXT NOT NULL,
    resource_path TEXT NOT NULL,
    action TEXT NOT NULL CHECK (action IN ('read', 'write', 'exec')),

    PRIMARY KEY (user_login, resource_path, action),

    FOREIGN KEY (user_login) REFERENCES users(login) ON DELETE CASCADE,
    FOREIGN KEY (resource_path) REFERENCES resources(path) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_permissions_user_res
ON permissions(user_login, resource_path);
