package com.dawn.payment.model;

import com.dawn.common.constant.PaymentMethod;
import com.dawn.common.constant.PaymentStatus;
import com.dawn.common.model.AbstractMappedEntity;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Hidden
@Entity
@Table(name = "payment")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Payment extends AbstractMappedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reservation_id", nullable = false)
    private String reservationId;

    @Column(name = "payment_intent_id", nullable = false)
    private String paymentIntentId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "method", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

}
