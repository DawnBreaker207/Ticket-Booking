package com.dawn.backend.dto.request;

import com.dawn.backend.constant.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationUserRequestDTO {

    Boolean isPaid;

    ReservationStatus status;
}
