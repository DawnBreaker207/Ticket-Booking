package com.example.backend.helper;

import com.example.backend.dto.shared.OrderDTO;
import com.example.backend.dto.shared.OrderSeatDTO;
import com.example.backend.model.Order;
import com.example.backend.model.OrderSeat;

import java.util.List;
import java.util.stream.Collectors;

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
	return new OrderSeatDTO(seat.getSeat() != null ? seat.getSeat() : null, seat.getPrice());
    }

    static OrderSeat map(OrderSeatDTO seatDto) {
	OrderSeat seat = new OrderSeat();
	if (seatDto.getSeat() != null) {
	    try {
		seat.setSeat(seatDto.getSeat());
		seat.setPrice(seatDto.getPrice());
	    } catch (NumberFormatException ex) {
		seat.setSeat(null);
	    }
	}

	return seat;
    }
}
