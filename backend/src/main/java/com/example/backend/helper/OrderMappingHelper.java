package com.example.backend.helper;

import com.example.backend.dto.response.OrderResponseDTO;
import com.example.backend.dto.shared.OrderSeatDTO;
import com.example.backend.model.Order;
import com.example.backend.model.OrderSeat;

import java.util.List;
import java.util.stream.Collectors;

public interface OrderMappingHelper {
    static OrderResponseDTO map(final Order o) {
        OrderResponseDTO order =
                OrderResponseDTO
                        .builder()
                        .orderId(o.getOrderId())
                        .userId(o.getUserId())
                        .cinemaHall(o.getCinemaHall())
                        .totalAmount(o.getTotalAmount())
                        .orderStatus(o.getOrderStatus())
                        .paymentMethod(o.getPaymentMethod())
                        .paymentStatus(o.getPaymentStatus())
                        .createdAt(o.getCreatedAt())
                        .updatedAt(o.getUpdatedAt())
                        .build();
        if (o.getSeats() != null) {
            List<OrderSeatDTO> seats = o.getSeats().stream().map(OrderMappingHelper::map).collect(Collectors.toList());
            order.setSeats(seats);
        }
        return order;
    }

    static Order map(final OrderResponseDTO o) {
        Order order = Order
                .builder()
                .orderId(o.getOrderId())
                .userId(o.getUserId())
                .cinemaHall(o.getCinemaHall())
                .totalAmount(o.getTotalAmount())
                .orderStatus(o.getOrderStatus())
                .paymentMethod(o.getPaymentMethod())
                .paymentStatus(o.getPaymentStatus())
                .createdAt(o.getCreatedAt())
                .updatedAt(o.getUpdatedAt())
                .build();
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
