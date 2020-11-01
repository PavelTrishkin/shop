INSERT INTO users
(id, archive, email, name, password, role)
VALUES (1, false, 'petr@mail.ru', 'petr', 'pass', 'CLIENT');

INSERT INTO users
(id, archive, email, name, password, role)
VALUES (2, false, 'tom@mail.ru', 'tom', 'pass', 'MANAGER');

INSERT INTO users
(id, archive, email, name, password, role)
VALUES (3, false, 'admin@mail.ru', 'admin', 'adminpass', 'ADMIN');

ALTER SEQUENCE user_seq RESTART WITH 4;