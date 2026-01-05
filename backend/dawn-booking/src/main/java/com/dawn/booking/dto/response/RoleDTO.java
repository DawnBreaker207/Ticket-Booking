package com.dawn.booking.dto.response;

import com.dawn.common.core.constant.URole;
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
