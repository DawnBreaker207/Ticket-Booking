package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


public class PaymentResponseDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class VNPayResponse {

        public String code;

        public String message;

        public String paymentUrl;
    }
}
