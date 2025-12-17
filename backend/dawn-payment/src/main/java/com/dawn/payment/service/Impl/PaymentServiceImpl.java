package com.dawn.payment.service.Impl;

import com.dawn.common.constant.Message;
import com.dawn.common.constant.PaymentStatus;
import com.dawn.payment.config.payment.MomoConfig;
import com.dawn.payment.config.payment.VNPayConfig;
import com.dawn.payment.dto.request.PaymentRequest;
import com.dawn.payment.dto.request.PaymentUpdateRequest;
import com.dawn.payment.dto.response.PaymentResponse;
import com.dawn.payment.model.Payment;
import com.dawn.payment.repository.PaymentRepository;
import com.dawn.payment.service.PaymentService;
import com.dawn.payment.utils.MomoUtils;
import com.dawn.payment.utils.VNPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final VNPayConfig VNPayConfig;

    private final MomoConfig momoConfig;

    private final RestClient restClient;

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(
            VNPayConfig VNPayConfig,
            MomoConfig momoConfig,
            PaymentRepository paymentRepository,
            @Qualifier("baseRestClient") RestClient.Builder builder) {
        this.VNPayConfig = VNPayConfig;
        this.momoConfig = momoConfig;
        this.paymentRepository = paymentRepository;
        this.restClient = builder.build();
    }

    public PaymentResponse createPayment(PaymentRequest req, HttpServletRequest request) {
        log.info("Payment request received: {}", req);
        PaymentResponse response;
        switch (req.getPaymentType().trim().toLowerCase()) {
            case "vnpay" -> response = createVNPayPayment(req.getReservationId(), req.getAmount(), request);
            case "momo" -> response = createMomoPayment(req.getReservationId(), req.getAmount());
            default -> throw new IllegalArgumentException("Unsupported payment provider " + req.getPaymentType());
        }

        return response;
    }

    @Transactional
    public Payment updatePayment(PaymentUpdateRequest request) {
        PaymentStatus status = request.getIsSuccess() ? PaymentStatus.PAID : PaymentStatus.CANCELED;
        paymentRepository.findByReservationId(request.getReservationId())
                .filter(p -> p.getStatus() == PaymentStatus.PAID)
                .ifPresent((payment) -> {
                    throw new IllegalStateException(Message.Exception.PAYMENT_COMPLETE);
                });

        Payment payment = Payment
                .builder()
                .reservationId(request.getReservationId())
                .paymentIntentId(request.getReservationId())
                .amount(request.getTotalAmount())
                .status(status)
                .createdAt(Instant.now())
                .build();
        return paymentRepository.saveAndFlush(payment);
    }

    private PaymentResponse createVNPayPayment(String reservationId, Integer totalPrice, HttpServletRequest clientIp) {
        Map<String, String> vnpParamsMap = VNPayConfig.getVNPayConfig();
        long amount = totalPrice * 100L;

        //  Config default bankCode
        //  This bank code is fixed, you can change it
        vnpParamsMap.put("vnp_BankCode", "NCB");
        vnpParamsMap.put("vnp_TxnRef", reservationId);
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        //  Update Ip Address
        // You can set default IP to "127.0.0.1" or "0.0.0.0" without get IP utils
        vnpParamsMap.put("vnp_IpAddr", VNPayUtils.getIpAddress(clientIp));
        //	Build Query URL
        String queryUrl = VNPayUtils.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtils.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtils.hmacSHA512(VNPayConfig.getVnp_SecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = VNPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return PaymentResponse
                .builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl)
                .build();
    }

    private PaymentResponse createMomoPayment(String reservationId, Integer total) {
        Map<String, String> momoParamsMap = momoConfig.getMomoConfig();

        momoParamsMap.put("orderId", reservationId);
        momoParamsMap.put("orderInfo", "Thanh toan don hang: " + reservationId);
        momoParamsMap.put("requestId", UUID.randomUUID().toString());
        momoParamsMap.put("amount", String.valueOf(total));
        momoParamsMap.put("extraData", "Khong co khuyen mai");
        momoParamsMap.put("signature", MomoUtils.sign(momoParamsMap, momoConfig));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String momoApiUrl = momoConfig.getMomo_PayUrl() + "/create";
        Map<String, Object> response = restClient.post()
                .uri(momoApiUrl)
                .body(momoParamsMap)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        if (response == null || response.get("payUrl") == null) {
            throw new RuntimeException("MoMo payment failed or payUrl is null");
        }

        return PaymentResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl((String) response.get("payUrl"))
                .build();
    }
}
