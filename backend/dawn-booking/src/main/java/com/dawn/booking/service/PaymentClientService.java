package com.dawn.booking.service;

import com.dawn.booking.dto.request.PaymentRequestDTO;
import com.dawn.booking.dto.response.PaymentDTO;

public interface PaymentClientService {
    PaymentDTO updatePayment(PaymentRequestDTO request);
}
