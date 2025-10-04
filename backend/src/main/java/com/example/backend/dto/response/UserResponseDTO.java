package com.example.backend.dto.response;

import com.example.backend.model.AbstractMappedEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UserResponseDTO extends AbstractMappedEntity {

    private Long userId;
    private String username;
    private String email;
}
