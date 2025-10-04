package com.example.backend.dto.shared;

import com.example.backend.model.Seat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class OrderSeatDTO {
    private Seat seat;
    private BigDecimal price;


}
