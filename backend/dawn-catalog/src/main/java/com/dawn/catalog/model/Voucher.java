package com.dawn.catalog.model;

import com.dawn.common.model.AbstractMappedEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Table(name = "vouchers")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Voucher extends AbstractMappedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    private String code;
    private String name;

    private VoucherCategory category;
    private String groupRef;

    private DiscountType discountType;

    private Long discountValue;
    private Long maxDiscountAmount;
    private Long minOrderValue;

    private Long quantityTotal;
    private Long quantityUsed;

    private Instant startAt;
    private Instant endAt;

    private Boolean isActive;

    private enum DiscountType {
        FIXED, PERCENT
    }

    private enum VoucherCategory {
        CAMPAIGN, SYSTEM
    }
}
