package com.example.backend.helper;

import java.util.List;
import java.util.stream.Collectors;

import com.example.backend.dto.OrderDTO;
import com.example.backend.dto.OrderSeatDTO;
import com.example.backend.model.Order;
import com.example.backend.model.OrderSeat;

public interface OrderMappingHelper {
    static OrderDTO map(final Order o) {
	OrderDTO order = new OrderDTO();
	order.setUserId(o.getUserId());
	order.setCinemaHallId(o.getCinemaHallId());
	order.setTotalAmount(o.getTotalAmount());
	order.setOrderStatus(o.getOrderStatus());
	order.setPaymentMethod(o.getPaymentMethod());
	order.setPaymentStatus(o.getPaymentStatus());

	if (o.getSeats() != null) {
	    List<OrderSeatDTO> seats = o.getSeats().stream().map(OrderMappingHelper::map).collect(Collectors.toList());
	    order.setSeats(seats);
	}
	return order;
    }

    static Order map(final OrderDTO o) {
	Order order = new Order();
	order.setUserId(o.getUserId());
	order.setCinemaHallId(o.getCinemaHallId());
	order.setTotalAmount(o.getTotalAmount());
	order.setOrderStatus(o.getOrderStatus());
	order.setPaymentMethod(o.getPaymentMethod());
	order.setPaymentStatus(o.getPaymentStatus());

	if (o.getSeats() != null) {
	    List<OrderSeat> seats = o.getSeats().stream().map(OrderMappingHelper::map).collect(Collectors.toList());
	    order.setSeats(seats);

	}
	return order;
    }

    static OrderSeatDTO map(OrderSeat seat) {
	return new OrderSeatDTO(seat.getSeatId() != null ? seat.getSeatId() : null, seat.getPrice());
    }

    static OrderSeat map(OrderSeatDTO seatDto) {
	OrderSeat seat = new OrderSeat();
	if (seatDto.getSeatId() != null) {
	    try {
		seat.setSeatId(seatDto.getSeatId());
		seat.setPrice(seatDto.getPrice());
	    } catch (NumberFormatException ex) {
		seat.setSeatId(null);
	    }
	}

	return seat;
    }
}
