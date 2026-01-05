package com.dawn.cinema.dto.request;

import com.dawn.common.core.dto.request.DatetimeFilterRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ShowtimeFilterRequest extends DatetimeFilterRequest {
    private Long theaterId;

    private Long movieId;
}
