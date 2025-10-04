package com.example.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {

    @NotBlank(message = "Username is not mandatory")
    @Size(min = 1, max = 100)
    private String username;


    @NotBlank(message = "Email is not mandatory")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Role is not mandatory")
    private Set<String> role;

    @NotBlank(message = "Password is not mandatory")
    @Min(value = 6, message = "Password is required 8 characters above ")
    private String password;


}
