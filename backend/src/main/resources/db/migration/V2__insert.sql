INSERT IGNORE INTO roles (name)
VALUES ('USER'),
       ('MODERATOR'),
       ('ADMIN');

INSERT IGNORE INTO user (username, password)
VALUES ('admin','admin')