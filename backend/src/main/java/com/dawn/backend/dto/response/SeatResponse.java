package com.dawn.backend.dto.response;

import com.dawn.backend.constant.SeatStatus;
import com.dawn.backend.model.AbstractMappedEntity;
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
public class SeatResponse extends AbstractMappedEntity {

    private Long id;

    private Long showtimeId;

    private String seatNumber;

    private SeatStatus status;

}