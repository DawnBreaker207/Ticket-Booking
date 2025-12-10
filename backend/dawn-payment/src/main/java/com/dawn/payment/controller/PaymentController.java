package com.dawn.payment.controller;

import com.dawn.common.config.response.ResponseObject;
import com.dawn.payment.dto.request.PaymentRequest;
import com.dawn.payment.dto.response.PaymentResponse;
import com.dawn.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@Tag(name = "Payment", description = "Operations related to payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/create")
    public ResponseObject<PaymentResponse> createPayment(@ModelAttribute PaymentRequest req, HttpServletRequest request) {
        return ResponseObject.success(paymentService.createPayment(req, request));
    }

//    @GetMapping("/vnpay-return")
//    public ResponseObject<PaymentResponseDTO.VNPayResponse> payCallbackHandler(HttpServletRequest req) {
//        String status = req.getParameter("vnp_ResponseCode");
//        if (status.equals("00")) {
//            return new ResponseObject<>(HttpStatus.OK, "Success", new PaymentResponseDTO.VNPayResponse("00", "Success", ""));
//        } else {
//            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Failed", null);
//        }
//    }
}
