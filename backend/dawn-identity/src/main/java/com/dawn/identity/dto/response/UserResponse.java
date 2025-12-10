package com.dawn.identity.dto.response;

import com.dawn.backend.dto.response.BaseResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UserResponse extends BaseResponse {

    private Long userId;

    private String avatar;

    private String username;

    private String email;

    private String role;

    private Boolean isDeleted;
}
