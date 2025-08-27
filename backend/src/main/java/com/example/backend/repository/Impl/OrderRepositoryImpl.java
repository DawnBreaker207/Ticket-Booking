package com.example.backend.repository.Impl;

import com.example.backend.constant.OrderStatus;
import com.example.backend.constant.PaymentMethod;
import com.example.backend.constant.PaymentStatus;
import com.example.backend.constant.SeatStatus;
import com.example.backend.exception.wrapper.OrderNotFoundException;
import com.example.backend.model.Order;
import com.example.backend.model.OrderSeat;
import com.example.backend.repository.OrderRepository;
import com.example.backend.util.OrderUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final DataSource datasource;

    public OrderRepositoryImpl(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public List<Order> findAll() {
        String sql = "SELECT * " + "FROM orders o " + "INNER JOIN order_seat os ON o.id = os.order_id ";
        Map<String, Order> orders = new HashMap<>();
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql); var rs = pre.executeQuery()) {
            while (rs.next()) {
                String orderId = rs.getString("o.id");
                var order = orders.get(orderId);

                if (order == null) {
                    order = new Order();
                    order.setOrderId(orderId);
                    order.setUserId(rs.getLong("user_id"));
                    order.setCinemaHallId(rs.getLong("cinema_hall_id"));
                    order.setOrderStatus(OrderStatus.valueOf(rs.getString("status")));
                    order.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")));
                    order.setPaymentStatus(PaymentStatus.valueOf(rs.getString("payment_status")));
                    order.setTotalAmount(rs.getBigDecimal("total_amount"));
                    order.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                    order.setSeats(new ArrayList<>());
                    orders.put(orderId, order);
                }
                OrderSeat orderSeat = new OrderSeat();
                orderSeat.setId(rs.getLong("os.id"));
                orderSeat.setOrderId(rs.getString("order_id"));
                orderSeat.setSeatId(rs.getLong("seat_id"));
                orderSeat.setPrice(rs.getBigDecimal("price"));
                order.getSeats().add(orderSeat);
            }
            return new ArrayList<>(orders.values());
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<Order> findOne(String id) {
        String sql = "SELECT * FROM orders o INNER JOIN order_seat os ON o.id = os.order_id WHERE o.id = ?";
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, id);

            try (var rs = pre.executeQuery()) {
                Order order = null;

                while (rs.next()) {
                    if (order == null) {
                        order = new Order();
                        order.setOrderId(rs.getString("o.id"));
                        order.setUserId(rs.getLong("user_id"));
                        order.setCinemaHallId(rs.getLong("cinema_hall_id"));
                        order.setOrderStatus(OrderStatus.valueOf(rs.getString("status")));
                        order.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")));
                        order.setPaymentStatus(PaymentStatus.valueOf(rs.getString("payment_status")));
                        order.setTotalAmount(rs.getBigDecimal("total_amount"));
                        order.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                        order.setSeats(new ArrayList<>());
                    }
                    Long seatOrderId = rs.getLong("os.id");
                    if (!rs.wasNull()) {
                        OrderSeat orderSeat = new OrderSeat();
                        orderSeat.setId(seatOrderId);
                        orderSeat.setOrderId(rs.getString("order_id"));
                        orderSeat.setSeatId(rs.getLong("seat_id"));
                        orderSeat.setPrice(rs.getBigDecimal("price"));
                        order.getSeats().add(orderSeat);

                    }
                }
                return order != null ? Optional.of(order) : Optional.empty();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<Order> findFirstByCustomerIdOrderByCreatedAtDesc(Long customerId) {
        String sql = "";
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql)) {
            return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Order save(Order o) {
        String insertOrder = "INSERT INTO orders (id,user_id, cinema_hall_id, status, payment_method, payment_status, total_amount, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String insertSeat = "INSERT INTO order_seat (order_id, seat_id, price) VALUES (?, ?, ?)";
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(insertOrder)) {
            conn.setAutoCommit(false);
            String generateOrderId = OrderUtils.generateOrderIds();
            o.setOrderId(generateOrderId);

            pre.setString(1, o.getOrderId());
            pre.setLong(2, o.getUserId());
            pre.setLong(3, o.getCinemaHallId());
            pre.setString(4, o.getOrderStatus().name());
            pre.setString(5, o.getPaymentMethod().name());
            pre.setString(6, o.getPaymentStatus().name());
            pre.setBigDecimal(7, o.getTotalAmount());
            pre.setTimestamp(8, Timestamp.from(o.getCreatedAt()));

            pre.executeUpdate();

//	    Insert seats
            try (var seatStmt = conn.prepareStatement(insertSeat, Statement.RETURN_GENERATED_KEYS)) {
                for (OrderSeat seat : o.getSeats()) {
                    seatStmt.setString(1, o.getOrderId());
                    seatStmt.setLong(2, seat.getSeatId());
                    seatStmt.setBigDecimal(3, seat.getPrice());
                    seatStmt.addBatch();
                }

                seatStmt.executeBatch();

                try (var rs = seatStmt.getGeneratedKeys()) {
                    int index = 0;
                    while (rs.next()) {
                        o.getSeats().get(index).setId(rs.getLong(1));
                        index++;

                    }
                }

                updateSeatStatusByOrderId(conn, o.getOrderId(), SeatStatus.BOOKED);
            }
            conn.commit();
            return findOne(o.getOrderId())
                    .orElseThrow(() -> new OrderNotFoundException(HttpStatus.NOT_FOUND, "Order not found after create"));

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

    }

    @Override
    public Order update(Order o) {
        String sql = "UPDATE orders SET status = ?, paymet_method = ?, payment_status = ? WHERE id = ?";
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            pre.setString(1, o.getOrderStatus().name());
            pre.setString(2, o.getPaymentMethod().name());
            pre.setString(3, o.getPaymentStatus().name());
            pre.setString(4, o.getOrderId());
            int affected = pre.executeUpdate();

            if (affected == 0) {
                conn.rollback();
                throw new OrderNotFoundException(HttpStatus.NOT_FOUND, "No order updated");
            }

            if (o.getOrderStatus() == OrderStatus.CANCELLED) {
                updateSeatStatusByOrderId(conn, o.getOrderId(), SeatStatus.AVAILABLE);
            }
            conn.commit();
            return findOne(o.getOrderId())
                    .orElseThrow(() -> new OrderNotFoundException(HttpStatus.NOT_FOUND, "Order not found after update"));

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            pre.setString(1, id);
            int rows = pre.executeUpdate();
            if (rows == 0) {
                conn.rollback();
                throw new OrderNotFoundException(HttpStatus.NOT_FOUND, "Not match found with id " + id);
            }
            conn.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

    }

    @Override
    public boolean findOrderSeatsExisted(List<OrderSeat> seats) {
        if (seats == null || seats.isEmpty())
            return false;
        String seatJoin = seats.stream().map(id -> "?").collect(Collectors.joining(","));
        String sql = "SELECT COUNT(*) FROM order_seat os " + "JOIN orders o ON os.order_id = o.id "
                + "WHERE os.seat_id IN (" + seatJoin + ") " + "AND o.status = 'CONFIRMED'";
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql)) {
            int i = 1;
            for (OrderSeat seat : seats) {
                pre.setLong(i++, seat.getSeatId());
            }

            try (var rs = pre.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            return false;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    private void updateSeatStatusByOrderId(Connection conn, String orderId, SeatStatus status) {
        String sql = "UPDATE seat SET status = ? WHERE id IN " + "(SELECT seat_id FROM order_seat WHERE order_id = ?)";
        try (var pre = conn.prepareStatement(sql)) {
            pre.setString(1, status.name());
            pre.setString(2, orderId);
            pre.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}
