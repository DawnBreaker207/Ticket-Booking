package com.dawn.backend.dto.response;

import com.dawn.backend.constant.ReservationStatus;
import com.dawn.backend.model.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ReservationResponse extends AbstractMappedEntity {
    private String id;

    private UserResponse user;

    private ShowtimeResponse showtime;

    private ReservationStatus reservationStatus;

    private BigDecimal totalAmount;

    private List<SeatResponse> seats = new ArrayList<>();

    private Boolean isDeleted;

    private Boolean isPaid;
}
