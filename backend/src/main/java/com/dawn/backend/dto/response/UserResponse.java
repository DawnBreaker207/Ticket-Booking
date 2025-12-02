package com.dawn.backend.dto.response;

import com.dawn.backend.model.AbstractMappedEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UserResponse extends AbstractMappedEntity {

    private Long userId;

    private String username;

    private String email;

    private Boolean isDeleted;
}
