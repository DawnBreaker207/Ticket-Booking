package com.example.backend.dto.response;

import com.example.backend.model.AbstractMappedEntity;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class UserResponseDTO extends AbstractMappedEntity {

    private Long userId;
    private String username;
    private String email;
    private String password;


}
