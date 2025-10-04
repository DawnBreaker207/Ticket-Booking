package com.example.backend.dto.response;

import com.example.backend.model.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO extends AbstractMappedEntity {

    private Long userId;
    private String username;
    private String email;
    private String password;


}
