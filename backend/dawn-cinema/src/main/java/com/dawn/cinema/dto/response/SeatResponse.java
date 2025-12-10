package com.dawn.cinema.dto.response;

import com.dawn.common.constant.SeatStatus;
import com.dawn.common.dto.response.BaseResponse;
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
public class SeatResponse extends BaseResponse {

    private Long id;

    private Long showtimeId;

    private String seatNumber;

    private SeatStatus status;

}