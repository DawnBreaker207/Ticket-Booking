package com.example.backend.repository;

import com.example.backend.dto.request.OrderFilterDTO;
import com.example.backend.dto.response.OrderResponseDTO;
import com.example.backend.model.Order;
import com.example.backend.model.OrderSeat;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    @Query(value = """
              SELECT DISTINCT o FROM Order AS o
                   LEFT JOIN FETCH o.seats AS os
                   LEFT JOIN FETCH os.seat AS s
                   LEFT JOIN FETCH o.cinemaHall AS ch
                   LEFT JOIN FETCH ch.movie as m
                   WHERE
                         (:#{#order.getQuery()} IS NULL OR o.orderId LIKE CONCAT('%', :#{#order.getQuery()}, '%') )
                         AND (:#{#order.getUserId()} IS NULL OR o.userId  =:#{#order.getUserId()})
                         AND (:#{#order.getCinemaHallId()} IS NULL OR o.cinemaHall.id  =:#{#order.getCinemaHallId()})
                         AND (:#{#order.getOrderStatus()} IS NULL OR o.orderStatus = :#{#order.getOrderStatus()})   
                         AND (
                            :#{#order.getStartDate()} IS NULL 
                            OR :#{#order.getEndDate()} IS NULL  
                            OR ( o.createdAt  BETWEEN :#{#order.getStartDate()} AND :#{#order.getEndDate()}) )
                         AND (:#{#order.getPaymentMethod()} IS NULL OR o.paymentMethod = :#{#order.getPaymentMethod()})
                         AND (:#{#order.getTotalAmount()} IS NULL OR o.totalAmount = :#{#order.getTotalAmount()})
                    ORDER BY 
                        CASE WHEN :#{#order.getSortBy()} = 'oldest' THEN o.createdAt END ASC,
                        CASE WHEN :#{#order.getSortBy()} = 'newest' THEN o.createdAt END DESC
            """)
    List<Order> findAllWithFilter(OrderFilterDTO order);


//    @Query(value = """
//            SELECT o.id          AS order_id,
//                o.user_id,
//                o.cinema_hall_id,
//                o.status         AS order_status,
//                o.payment_method,
//                o.payment_status,
//                o.total_amount,
//                o.created_at,
//                o.updated_at,
//                os.id            AS os_id,
//                os.order_id      AS os_order_id,
//                os.price         AS os_price,
//                s.id             AS seat_id,
//                s.cinema_hall_id AS seat_cinema_hall_id,
//                s.seat_number    AS seat_number,
//                s.status         AS seat_status,
//                s.price          AS seat_price
//            FROM orders AS o
//                JOIN order_seat AS os ON o.id = os.order_id
//                JOIN seat AS s ON os.seat_id = s.id
//            ORDER BY created_at DESC
//            """,
//            nativeQuery = true)
//    List<Order> findAll();

//    @Query(value = """
//               SELECT o.id          AS order_id,
//                o.user_id,
//                o.cinema_hall_id,
//                o.status         AS order_status,
//                o.payment_method,
//                o.payment_status,
//                o.total_amount,
//                o.created_at,
//                o.updated_at,
//                os.id            AS os_id,
//                os.order_id      AS os_order_id,
//                os.price         AS os_price,
//                s.id             AS seat_id,
//                s.cinema_hall_id AS seat_cinema_hall_id,
//                s.seat_number    AS seat_number,
//                s.status         AS seat_status,
//                s.price          AS seat_price
//            FROM orders AS o
//                JOIN order_seat AS os ON o.id = os.order_id
//                JOIN seat AS s ON os.seat_id = s.id
//            WHERE o.id = :#{#s}
//            """, nativeQuery = true)
//    Optional<Order> findById(String s);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM orders WHERE id = :id", nativeQuery = true)
    void delete(String id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO order_seat (order_id, seat_id, price) VALUE (:#{#seat.orderId}, :#{#seat.seat.id}, :#{#seat.price})", nativeQuery = true)
    void insertOrderSeat(OrderSeat seat);

    @Modifying
    @Transactional
    @Query(value = """
                    UPDATE seat 
                    SET status = :status 
                    WHERE id IN 
                    (SELECT seat_id FROM order_seat WHERE order_id = :orderid)
            """, nativeQuery = true)
    void updateOrderSeat(String orderId, String status);

    Optional<Order> findFirstByUserIdOrderByCreatedAtDesc(Long userId);

    @Modifying
    @Transactional
    @Query(value = """
                    SELECT 
                        CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
                    FROM order_seat os 
                    JOIN orders o ON os.order_id = o.id
                    WHERE os.seat IN :seatIds
                    AND o.status = 'CONFIRMED'
            """, nativeQuery = true)
    boolean findOrderSeatsExisted(List<Long> seatIds);
}

