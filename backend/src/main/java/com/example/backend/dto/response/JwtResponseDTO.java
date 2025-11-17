package com.example.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class JwtResponseDTO {
    private String accessToken;

    @JsonIgnore
    private String refreshToken;

    private Long userId;

    private String username;

    private String email;
}
