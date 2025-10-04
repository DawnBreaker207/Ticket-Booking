package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponseDTO {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Long userId;
    private String username;
    private String email;
    private List<String> roles;

}
