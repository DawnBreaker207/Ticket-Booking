package com.dawn.booking.dto.response;

import com.dawn.common.constant.ReservationStatus;
import com.dawn.common.dto.response.BaseResponse;
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
public class ReservationResponse extends BaseResponse {
    private String id;

    private Long userId;

    private Long showtimeId;

    private ReservationStatus reservationStatus;

    private BigDecimal totalAmount;

    private List<String> seats = new ArrayList<>();

    private Boolean isDeleted;

    private Boolean isPaid;
}
