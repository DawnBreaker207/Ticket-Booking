package com.example.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDTO {
    @NotBlank(message = "Username is not mandatory")
    @Size(min = 1, max = 100)
    private String username;

    @NotBlank(message = "Password is not mandatory")
    @Min(value = 6, message = "Password is required 8 characters above")
    private String password;

}
