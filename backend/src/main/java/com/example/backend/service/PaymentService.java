package com.example.backend.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.backend.config.payment.VNPayConfig;
import com.example.backend.dto.shared.PaymentDTO;
import com.example.backend.util.VNPayUtils;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class PaymentService {
    private final VNPayConfig VNPayConfig;

    public PaymentService(VNPayConfig VNPayConfig) {
        this.VNPayConfig = VNPayConfig;
    }

    public PaymentDTO.VNPayResponse createVNPayPayment(HttpServletRequest req) {
        long amount = Integer.parseInt(req.getParameter("amount")) * 100L;
        String bankCode = req.getParameter("bankCode");
        Map<String, String> vnpParamsMap = VNPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }

        vnpParamsMap.put("vnp_IpAddr", VNPayUtils.getIpAddress(req));
//	Build Query URL
        String queryUrl = VNPayUtils.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtils.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtils.hmacSHA512(VNPayConfig.getVnp_secretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = VNPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return new PaymentDTO.VNPayResponse("ok", "success", paymentUrl);
    }
}
