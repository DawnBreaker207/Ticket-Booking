CREATE TABLE IF NOT EXISTS users
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    username   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS movie
(
    id           INT PRIMARY KEY AUTO_INCREMENT,
    title        VARCHAR(255) NOT NULL,
    poster       VARCHAR(255),
    overview     TEXT,
    duration     INT          NOT NULL,
    genres       VARCHAR(100),
    release_date DATE,
    imdb_id      VARCHAR(255),
    film_id      VARCHAR(255),
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cinema_hall
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    movie_id      INT      NOT NULL,
    movie_session DATETIME NOT NULL,
    created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_hall_movie FOREIGN KEY (movie_id) REFERENCES movie (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS seat
(
    id             INT PRIMARY KEY AUTO_INCREMENT,
    cinema_hall_id INT            NOT NULL,
    price          DECIMAL(10, 2) NOT NULL      DEFAULT 0.00,
    seat_number    VARCHAR(20)    NOT NULL,
    status         ENUM ('AVAILABLE', 'BOOKED') DEFAULT 'AVAILABLE',
    CONSTRAINT fk_seat_hall FOREIGN KEY (cinema_hall_id) REFERENCES cinema_hall (id) ON DELETE CASCADE,
    UNIQUE (cinema_hall_id, seat_number)
);

CREATE TABLE IF NOT EXISTS orders
(
    id             VARCHAR(50) PRIMARY KEY,
    user_id        BIGINT         NOT NULL,
    cinema_hall_id INT            NOT NULL,
    status         ENUM ('CREATED','CONFIRMED','CANCELLED')  DEFAULT 'CREATED',
    payment_method ENUM ('MOMO', 'VNPAY', 'ZALOPAY', 'CASH') DEFAULT 'CASH',
    payment_status ENUM ('PENDING', 'PAID', 'CANCELLED')     DEFAULT 'PENDING',
    total_amount   DECIMAL(10, 2) NOT NULL                   DEFAULT 0.00,
    created_at     DATETIME       NOT NULL                   DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME       NOT NULL                   DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_order_hall FOREIGN KEY (cinema_hall_id) REFERENCES cinema_hall (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS order_seat
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    order_id VARCHAR(50)    NOT NULL,
    seat_id  INT            NOT NULL,
    price    DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_order_seat_order FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    CONSTRAINT fk_order_seat FOREIGN KEY (seat_id) REFERENCES seat (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS roles
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    name       ENUM ('USER', 'MODERATOR','ADMIN') DEFAULT 'USER',
    created_at DATETIME NOT NULL                  DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL                  DEFAULT CURRENT_TIMESTAMP

);

CREATE TABLE IF NOT EXISTS user_role
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_role_user FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS refresh_token
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id     BIGINT       NOT NULL,
    token       VARCHAR(512) NOT NULL UNIQUE,
    expiry_date DATETIME     NOT NULL,
    CONSTRAINT fk_user_refresh FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);