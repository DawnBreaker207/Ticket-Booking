package com.example.backend.dto.response;

import com.example.backend.constant.OrderStatus;
import com.example.backend.constant.PaymentMethod;
import com.example.backend.constant.PaymentStatus;
import com.example.backend.dto.shared.OrderSeatDTO;
import com.example.backend.model.AbstractMappedEntity;
import com.example.backend.model.CinemaHall;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class OrderResponseDTO extends AbstractMappedEntity {
    private String orderId;

    private Long userId;

    private CinemaHall cinemaHall;

    private OrderStatus orderStatus;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private BigDecimal totalAmount;

    private List<OrderSeatDTO> seats = new ArrayList<>();


}
