INSERT IGNORE INTO roles (name)
VALUES ('USER'),
       ('MODERATOR'),
       ('ADMIN');

INSERT IGNORE INTO users (username, password)
VALUES ('admin','admin')