INSERT INTO users (id, archive, email, name, password, role, bucket_id)
values (1, false, 'mail@mail.ru', 'Admin', '$2a$10$34O3zGzgRJh3xa4ckH8UBuo6YiEHjAq40x4USglECgDuH3wL95bSW', 'ADMIN', null);
ALTER SEQUENCE user_seq RESTART WITH 2;