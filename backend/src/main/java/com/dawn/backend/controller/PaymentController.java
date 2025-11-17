package com.dawn.backend.controller;

import com.dawn.backend.config.response.ResponseObject;
import com.dawn.backend.dto.request.PaymentRequestDTO;
import com.dawn.backend.dto.response.PaymentResponseDTO;
import com.dawn.backend.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseObject<PaymentResponseDTO> createPayment(@ModelAttribute PaymentRequestDTO req, HttpServletRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createPayment(req, request));
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
