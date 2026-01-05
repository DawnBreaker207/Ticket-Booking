package com.dawn.report.dto.request;

import com.dawn.common.core.dto.request.DatetimeFilterRequest;
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
