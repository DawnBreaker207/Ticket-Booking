package com.dawn.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingNotificationEvent implements Serializable {
    private String to;
    private String name;
    private String reservationId;
    private String movieName;
    private String theaterName;
    private String showtimeSession;
    private String seats;
    private String paymentTime;
    private String total;
}
