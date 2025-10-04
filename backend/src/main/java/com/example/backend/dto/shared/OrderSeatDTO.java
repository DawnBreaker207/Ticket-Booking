package com.example.backend.dto.shared;

import com.example.backend.model.Seat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSeatDTO {
    private Seat seat;
    private BigDecimal price;


}
