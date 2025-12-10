package com.dawn.booking.dto.request;

import com.dawn.common.constant.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationUserRequest {
    private Long userId;

    private ReservationStatus status;
}
