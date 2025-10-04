package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TokenRefreshResponseDTO {
    private String accessToken;
    private String refreshToken;
    private String token = "Bearer ";


}
