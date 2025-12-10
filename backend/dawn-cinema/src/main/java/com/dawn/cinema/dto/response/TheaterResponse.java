package com.dawn.cinema.dto.response;

import com.dawn.common.dto.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class TheaterResponse extends BaseResponse {
    private Long id;

    private String name;

    private String location;

    private Integer capacity;

    private Boolean isDeleted;

    private List<ShowtimeResponse> showtime = new ArrayList<>();
}
