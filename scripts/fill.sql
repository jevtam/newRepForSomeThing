PRAGMA foreign_keys = ON;

--пользователи
INSERT OR IGNORE INTO users(login, salt, password_hash) VALUES
('alice', 'nan',    'a547590f99d5fd581b3ce00f83757bb2019ec16e54816418e1e932ddbd8afadc'),
('bob',   'pepper', '744a9101f7182a6ae0d978121ff74e33cac8d2832579c0637c1c37e9bbb6c065');

--ресурсы
INSERT OR IGNORE INTO resources(path, max_volume) VALUES
('A',        100),
('A.B',       60),
('A.B.C',     30),
('A.B.C.f_d', 10),
('A.A8B',     80);

--права
INSERT OR IGNORE INTO permissions(user_login, resource_path, action) VALUES
('alice', 'A',   'read'),
('alice', 'A.B', 'read'),
('alice', 'A.B', 'write'),
('bob',   'A.A8B', 'read');
