package com.example.backend.service;

import com.example.backend.config.payment.VNPayConfig;
import com.example.backend.dto.request.PaymentRequestDTO;
import com.example.backend.dto.response.PaymentResponseDTO;
import com.example.backend.util.VNPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final VNPayConfig VNPayConfig;

    public PaymentResponseDTO createPayment(PaymentRequestDTO req, HttpServletRequest request) {
        log.info("Payment request received: {}", req);
        PaymentResponseDTO response;
        switch (req.getPaymentType().trim().toLowerCase()) {
            case "vnpay" -> response = createVNPayPayment(req.getReservationId(), req.getAmount(), request);
            default -> throw new IllegalArgumentException("Unsupported payment provider " + req.getPaymentType());
        }

        return response;
    }

    private PaymentResponseDTO createVNPayPayment(String reservationId, Integer totalPrice, HttpServletRequest clientIp) {
        Map<String, String> vnpParamsMap = VNPayConfig.getVNPayConfig();
        long amount = totalPrice * 100L;

        //  Config default bankCode
        vnpParamsMap.put("vnp_BankCode", "NCB");
        vnpParamsMap.put("vnp_TxnRef", reservationId);
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        //  Update Ip Address
        vnpParamsMap.put("vnp_IpAddr", VNPayUtils.getIpAddress(clientIp));
        //	Build Query URL
        String queryUrl = VNPayUtils.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtils.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtils.hmacSHA512(VNPayConfig.getVnp_secretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = VNPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return PaymentResponseDTO
                .builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl)
                .build();
    }
}
