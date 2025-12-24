INSERT INTO users(username, salt, password_hash_hex) VALUES
 ('alice','nan','a547590f99d5fd581b3ce00f83757bb2019ec16e54816418e1e932dddb8afadc'),
 ('bob','pepper','744a9101f7182a6ae0d978121ff74e33cac8d2832579c0637c1c37e9bbb6c065');

INSERT INTO resources(path, max_volume) VALUES
 ('A',100),
 ('A.B',60),
 ('A.B.C',30),
 ('A.B.C.f_d',10),
 ('A.A8B',80);

INSERT INTO permissions(user_id, resource_path, actions)
SELECT u.id, 'A', 'read' FROM users u WHERE u.username='alice';

INSERT INTO permissions(user_id, resource_path, actions)
SELECT u.id, 'A.B', 'read,write' FROM users u WHERE u.username='alice';

INSERT INTO permissions(user_id, resource_path, actions)
SELECT u.id, 'A.A8B', 'read' FROM users u WHERE u.username='bob';
