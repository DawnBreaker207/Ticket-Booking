package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class PaymentResponseDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VNPayResponse {

        public String code;

        public String message;
        
        public String paymentUrl;
    }
}
