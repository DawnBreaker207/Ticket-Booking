package com.example.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RegisterRequestDTO {

    @NotBlank(message = "Username is not mandatory")
    @Size(min = 1, max = 100)
    private String username;


    @NotBlank(message = "Email is not mandatory")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Password is not mandatory")
    @Min(value = 6, message = "Password is required 8 characters above ")
    private String password;


}
