package com.dawn.payment.service;

import com.dawn.payment.dto.request.PaymentRequest;
import com.dawn.payment.dto.response.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest req, HttpServletRequest request);
}
