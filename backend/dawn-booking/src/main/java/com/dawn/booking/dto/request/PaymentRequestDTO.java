package com.dawn.booking.dto.request;

import com.dawn.common.model.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PaymentRequestDTO extends AbstractMappedEntity {
    private String reservationId;

    private BigDecimal totalAmount;

    private Boolean isSuccess;
}
