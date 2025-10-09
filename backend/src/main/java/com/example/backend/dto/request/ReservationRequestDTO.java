package com.example.backend.dto.request;

import com.example.backend.constant.ReservationStatus;
import com.example.backend.model.AbstractMappedEntity;
import com.example.backend.model.Seat;
import com.example.backend.model.Showtime;
import com.example.backend.model.User;
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
public class ReservationRequestDTO extends AbstractMappedEntity {

    private String reservationId;

    private Long showtimeId;

    private Long userId;

    private List<Long> seats = new ArrayList<>();
}
