package com.example.backend.repository.Impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.example.backend.constant.OrderStatus;
import com.example.backend.constant.PaymentMethod;
import com.example.backend.constant.PaymentStatus;
import com.example.backend.constant.SeatStatus;
import com.example.backend.exception.wrapper.OrderNotFoundException;
import com.example.backend.model.Order;
import com.example.backend.model.OrderSeat;
import com.example.backend.repository.OrderRepository;
import com.example.backend.util.OrderUtils;

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
		    order.setOrderTime(rs.getTimestamp("order_time").toLocalDateTime());
		    order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
		    order.setExpiredAt(rs.getTimestamp("expired_at").toLocalDateTime());
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
			order.setOrderTime(rs.getTimestamp("order_time").toLocalDateTime());
			order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
			order.setExpiredAt(rs.getTimestamp("expired_at").toLocalDateTime());
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
	String insertOrder = "INSERT INTO orders (id,user_id, cinema_hall_id, status, payment_method, payment_status, total_amount, order_time, created_at, expired_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
	    pre.setTimestamp(8, Timestamp.valueOf(o.getOrderTime()));
	    pre.setTimestamp(9, Timestamp.valueOf(o.getCreatedAt()));
	    pre.setTimestamp(10, Timestamp.valueOf(o.getExpiredAt()));
	    pre.executeUpdate();

//	    Insert seats
	    try (var seatStmt = conn.prepareStatement(insertSeat)) {
		for (OrderSeat seat : o.getSeats()) {
		    seatStmt.setString(1, o.getOrderId());
		    seatStmt.setLong(2, seat.getSeatId());
		    seatStmt.setBigDecimal(3, seat.getPrice());
		    seatStmt.addBatch();
		}

		seatStmt.executeBatch();
		updateSeatStatusByOrderId(conn, o.getOrderId(), SeatStatus.RESERVED);
	    }
	    conn.commit();
	    return findOne(o.getOrderId())
		    .orElseThrow(() -> new OrderNotFoundException("Order not found after create"));

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
		throw new OrderNotFoundException("No order updated");
	    }

	    if (o.getOrderStatus() == OrderStatus.CANCELLED) {
		updateSeatStatusByOrderId(conn, o.getOrderId(), SeatStatus.AVAILABLE);
	    }
	    conn.commit();
	    return findOne(o.getOrderId())
		    .orElseThrow(() -> new OrderNotFoundException("Order not found after update"));

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
		throw new OrderNotFoundException("Not match found with id " + id);
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
		+ "WHERE os.seat_id IN (" + seatJoin + ") " + "AND o.expired_at > ? " + "AND o.status = 'PENDING'";
	try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql)) {
	    int i = 1;
	    for (OrderSeat seat : seats) {
		pre.setLong(i++, seat.getSeatId());
	    }
	    pre.setTimestamp(i, Timestamp.valueOf(LocalDateTime.now()));

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

    @Override
    public List<Order> findPendingOrderExpired() {
	String sql = "SELECT * FROM orders WHERE status = ? AND expired_at < NOW()";
	List<Order> orders = new ArrayList<>();
	try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql)) {
	    pre.setString(1, "CREATED");

	    try (var rs = pre.executeQuery()) {
		while (rs.next()) {
		    Order o = new Order();
		    o.setOrderId(rs.getString("order_id"));
		    o.setUserId(rs.getLong("user_id"));
		    o.setCinemaHallId(rs.getLong("cinema_hall_id"));
		    o.setOrderStatus(OrderStatus.valueOf(rs.getString("status")));
		    o.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")));
		    o.setPaymentStatus(PaymentStatus.valueOf(rs.getString("payment_status")));
		    o.setTotalAmount(rs.getBigDecimal("total_amount"));
		    o.setOrderTime(rs.getTimestamp("order_time").toLocalDateTime());
		    o.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
		    o.setExpiredAt(rs.getTimestamp("expired_at").toLocalDateTime());
		    orders.add(o);
		}
	    }

	    return orders;
	} catch (SQLException ex) {
	    ex.printStackTrace();
	    throw new RuntimeException(ex);
	}
    }

    @Override
    public int updateExpiredOrder() {
	String select = "SELECT id FROM orders WHERE status = ? AND expired_at < NOW()";
	String update = "UPDATE orders SET status = ? WHERE id = ?";
	try (var conn = datasource.getConnection()) {
	    conn.setAutoCommit(false);
	    List<String> orderIds = new ArrayList<>();

	    try (var pre = conn.prepareStatement(select)) {
		pre.setString(1, OrderStatus.CREATED.name());
		try (var rs = pre.executeQuery()) {
		    while (rs.next()) {
			orderIds.add(rs.getString("id"));
		    }
		}
	    }

	    try (var preUpdate = conn.prepareStatement(update)) {
		for (String orderId : orderIds) {
		    preUpdate.setString(1, OrderStatus.EXPIRED.name());
		    preUpdate.setString(2, orderId);
		    preUpdate.executeUpdate();

		    updateSeatStatusByOrderId(conn, orderId, SeatStatus.AVAILABLE);
		}
	    }

	    conn.commit();
	    return orderIds.size();
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
