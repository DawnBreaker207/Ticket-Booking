package com.dawn.backend.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DashboardFilterRequest extends DatetimeFilterRequest {
    private Long movieId;

    private Long theaterId;
}
