package com.example.backend.controller;

import com.example.backend.config.response.ResponseObject;
import com.example.backend.dto.shared.PaymentDTO;
import com.example.backend.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@Tag(name = "Payment" , description = "Operations related to payment")
public class PaymentController {
    private final PaymentService paymentService;

    PaymentController(PaymentService paymentService) {
	this.paymentService = paymentService;
    }

    @GetMapping("/vnpay")
    public ResponseObject<PaymentDTO.VNPayResponse> pay(HttpServletRequest req) {
	return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVNPayPayment(req));
    }

    @GetMapping("/vnpay-return")
    public ResponseObject<PaymentDTO.VNPayResponse> payCallbackHandler(HttpServletRequest req) {
	String status = req.getParameter("vnp_ResponseCode");
	if (status.equals("00")) {
	    return new ResponseObject<>(HttpStatus.OK, "Success", new PaymentDTO.VNPayResponse("00", "Success", ""));
	} else {
	    return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Failed", null);
	}
    }
}
