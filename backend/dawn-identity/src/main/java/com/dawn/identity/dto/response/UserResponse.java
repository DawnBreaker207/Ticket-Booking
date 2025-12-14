package com.dawn.identity.dto.response;

import com.dawn.common.dto.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse extends BaseResponse {

    private Long userId;

    private String avatar;

    private String username;

    private String email;

    private String role;

    private Boolean isDeleted;
}
