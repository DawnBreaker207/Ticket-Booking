-- =====================================================
-- SEEDING DATA CHO HỆ THỐNG ĐẶT VÉ XEM PHIM (09-11/2025)
-- =====================================================

SET SESSION cte_max_recursion_depth = 1000;
SET
@now := NOW();

-- =====================================================
-- 1) TẠO LỊCH CHIẾU (SHOWTIME) CHO 3 THÁNG
-- =====================================================
INSERT
IGNORE INTO showtime (movie_id, theater_id, show_date, show_time, price, total_seats, available_seats)
WITH RECURSIVE
days AS (
    SELECT DATE('2025-09-01') AS d
    UNION ALL
    SELECT DATE_ADD(d, INTERVAL 1 DAY)
    FROM days
    WHERE d <= '2025-12-31'
),
times AS (
    SELECT '10:00:00' AS show_time
    UNION ALL SELECT '14:00:00'
    UNION ALL SELECT '18:00:00'
    UNION ALL SELECT '21:00:00'
),
movie_theater AS (
    SELECT 1 AS movie_id, 1 AS theater_id
    UNION ALL SELECT 1, 2
    UNION ALL SELECT 2, 3
    UNION ALL SELECT 2, 4
    UNION ALL SELECT 3, 5
    UNION ALL SELECT 4, 1
    UNION ALL SELECT 5, 2
    UNION ALL SELECT 5, 3
    UNION ALL SELECT 6, 4
    UNION ALL SELECT 7, 5
    UNION ALL SELECT 1, 3
    UNION ALL SELECT 2, 5
    UNION ALL SELECT 3, 1
    UNION ALL SELECT 3, 2
    UNION ALL SELECT 8, 1
    UNION ALL SELECT 8, 2
    UNION ALL SELECT 9, 3
    UNION ALL SELECT 9, 4
    UNION ALL SELECT 10, 5
    UNION ALL SELECT 5, 1
    UNION ALL SELECT 6, 2
    UNION ALL SELECT 6, 5
    UNION ALL SELECT 7, 3
    UNION ALL SELECT 4, 4
)
SELECT m.movie_id,
       m.theater_id,
       d.d                                                                                 AS show_date,
       t.show_time,
       CASE WHEN t.show_time IN ('18:00:00', '21:00:00') THEN 150000.00 ELSE 100000.00 END AS price,
       60                                                                                  AS total_seats,
       60                                                                                  AS available_seats
FROM movie_theater m
         CROSS JOIN days d
         CROSS JOIN times t
WHERE MONTH (d.d) = CASE
    WHEN m.movie_id BETWEEN 1
  AND 3 THEN 9
    WHEN m.movie_id BETWEEN 4
  AND 7 THEN 10
    ELSE 11
END;

-- =====================================================
-- 2) TẠO GHẾ NGỒI
-- =====================================================
INSERT INTO seat (showtime_id, seat_number, status, created_at, updated_at)
WITH RECURSIVE
    seat_rows AS (SELECT 'A' AS r
                  UNION ALL
                  SELECT 'B'
                  UNION ALL
                  SELECT 'C'
                  UNION ALL
                  SELECT 'D'
                  UNION ALL
                  SELECT 'E'
                  UNION ALL
                  SELECT 'F'),
    nums AS (SELECT 1 AS n
             UNION ALL
             SELECT 2
             UNION ALL
             SELECT 3
             UNION ALL
             SELECT 4
             UNION ALL
             SELECT 5
             UNION ALL
             SELECT 6
             UNION ALL
             SELECT 7
             UNION ALL
             SELECT 8
             UNION ALL
             SELECT 9
             UNION ALL
             SELECT 10)
SELECT s.id                                                      AS showtime_id,
       CONCAT(seat_rows.r, nums.n)                               AS seat_number,
       CASE WHEN RAND() < 0.7 THEN 'BOOKED' ELSE 'AVAILABLE' END AS status,
       @now                                                      AS created_at,
       @now                                                      AS updated_at
FROM showtime s
         CROSS JOIN seat_rows
         CROSS JOIN nums
WHERE NOT EXISTS (SELECT 1
                  FROM seat st
                  WHERE st.showtime_id = s.id
                    AND st.seat_number = CONCAT(seat_rows.r, nums.n));

-- =====================================================
-- 3) TẠO USER MẪU
-- =====================================================
INSERT
IGNORE INTO users (id, username, email, password, avatar, created_at, updated_at)
VALUES
(1, 'user1', 'user1@example.com', '$2a$10$8.4/JMOOazc6T4OQ0kMSiupd3MEqjl2nLNnfo0w17znlyF2sXeVMG', 'https://i.pravatar.cc/300?img=12', @now, @now),
(2, 'user2', 'user2@example.com', '$2a$10$8.4/JMOOazc6T4OQ0kMSiupd3MEqjl2nLNnfo0w17znlyF2sXeVMG', 'https://i.pravatar.cc/300?img=12', @now, @now),
(3, 'user3', 'user3@example.com', '$2a$10$8.4/JMOOazc6T4OQ0kMSiupd3MEqjl2nLNnfo0w17znlyF2sXeVMG', 'https://i.pravatar.cc/300?img=12', @now, @now),
(4, 'user4', 'user4@example.com', '$2a$10$8.4/JMOOazc6T4OQ0kMSiupd3MEqjl2nLNnfo0w17znlyF2sXeVMG', 'https://i.pravatar.cc/300?img=12', @now, @now),
(5, 'user5', 'user5@example.com', '$2a$10$8.4/JMOOazc6T4OQ0kMSiupd3MEqjl2nLNnfo0w17znlyF2sXeVMG', 'https://i.pravatar.cc/300?img=12', @now, @now);

INSERT
IGNORE INTO user_role (user_id, role_id)
SELECT id, 1
FROM users
WHERE id BETWEEN 1 AND 5;

-- =====================================================
-- 4) TẠO RESERVATION VÀ GÁN CHO SEAT BOOKED
-- =====================================================
INSERT INTO reservation (id, user_id, showtime_id, status, total_amount, is_paid, created_at, updated_at)
SELECT CONCAT('ORD-', UPPER(SUBSTRING(REPLACE(UUID(), '-', ''), 1, 12))) AS id,
       ((ABS(CRC32(CONCAT(s.showtime_id, s.seat_number))) MOD 5) + 1)    AS user_id,
       s.showtime_id,
       CASE WHEN RAND() < 0.85 THEN 'CONFIRMED' ELSE 'CANCELED' END      AS status,
       sh.price                                                          AS total_amount,
       CASE WHEN RAND() < 0.9 THEN TRUE ELSE FALSE END                   AS is_paid,
       DATE_SUB(sh.show_date, INTERVAL FLOOR(1 + RAND() * 7) DAY)        AS created_at,
       DATE_SUB(sh.show_date, INTERVAL FLOOR(1 + RAND() * 7) DAY)        AS updated_at
FROM seat s
         JOIN showtime sh ON sh.id = s.showtime_id
WHERE s.status = 'BOOKED'
  AND NOT EXISTS (SELECT 1
                  FROM reservation r
                  WHERE r.showtime_id = s.showtime_id
                    AND r.user_id = ((ABS(CRC32(CONCAT(s.showtime_id, s.seat_number))) MOD 5) + 1));

-- CẬP NHẬT SEAT.RESERVATION_ID THEO RESERVATION THỰC TẾ
UPDATE seat s
    JOIN reservation r
ON s.showtime_id = r.showtime_id
    AND ((ABS(CRC32(CONCAT(s.showtime_id, s.seat_number))) MOD 5) + 1) = r.user_id
    SET s.reservation_id = r.id
WHERE s.status = 'BOOKED';

-- =====================================================
-- 5) CẬP NHẬT AVAILABLE_SEATS
-- =====================================================
UPDATE showtime sh
SET available_seats = (SELECT COUNT(*)
                       FROM seat st
                       WHERE st.showtime_id = sh.id
                         AND st.status = 'AVAILABLE');

-- =====================================================
-- 6) TẠO PAYMENT
-- =====================================================
INSERT INTO payment (reservation_id, payment_intent_id, amount, method, status, created_at, updated_at)
SELECT r.id,
       CONCAT('PI_', UPPER(SUBSTRING(MD5(CONCAT(r.id, RAND())), 1, 24))) AS payment_intent_id,
       r.total_amount,
       CASE FLOOR(1 + RAND() * 3)
           WHEN 1 THEN 'MOMO'
           ELSE 'VNPAY'
           END                                                           AS method,
       CASE WHEN r.status = 'CONFIRMED' THEN 'PAID' ELSE 'CANCELED' END  AS status,
       r.created_at,
       r.updated_at
FROM reservation r
WHERE r.is_paid = TRUE
  AND NOT EXISTS (SELECT 1
                  FROM payment p
                  WHERE p.reservation_id = r.id);
