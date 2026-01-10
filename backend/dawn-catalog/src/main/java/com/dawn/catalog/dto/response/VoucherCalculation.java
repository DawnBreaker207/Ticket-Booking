package com.dawn.catalog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@SuperBuilder
public class VoucherCalculation {
    private String code;

    private BigDecimal discountAmount;

    private BigDecimal finalAmount;
}
