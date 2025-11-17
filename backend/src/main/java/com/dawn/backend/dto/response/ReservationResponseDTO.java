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
public class ReservationResponseDTO extends AbstractMappedEntity {
    private String id;

    private UserResponseDTO user;

    private ShowtimeResponseDTO showtime;

    private ReservationStatus reservationStatus;

    private BigDecimal totalAmount;

    private List<SeatResponseDTO> seats = new ArrayList<>();

    private Boolean isDeleted;

    private Boolean isPaid;
}
