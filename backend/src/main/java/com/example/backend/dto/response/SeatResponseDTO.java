package com.example.backend.dto.response;

import com.example.backend.constant.SeatStatus;
import com.example.backend.model.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SeatResponseDTO extends AbstractMappedEntity {

    private Long id;

    private Long showtimeId;

    private String seatNumber;

    private SeatStatus status;

}