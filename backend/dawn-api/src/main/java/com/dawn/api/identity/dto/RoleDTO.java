package com.dawn.api.identity.dto;

import com.dawn.common.constant.URole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RoleDTO {
    private URole name;
}
