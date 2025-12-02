package com.dawn.backend.helper;

import com.dawn.backend.dto.request.TheaterRequest;
import com.dawn.backend.dto.response.TheaterResponse;
import com.dawn.backend.model.Theater;

import java.util.Collections;
import java.util.Optional;

public interface TheaterMappingHelper {
    static Theater map(final TheaterRequest theater) {
        return Theater
                .builder()
                .name(theater.getName())
                .location(theater.getLocation())
                .capacity(theater.getCapacity())
                .build();
    }

    static TheaterResponse map(final Theater theater) {
        return TheaterResponse
                .builder()
                .id(theater.getId())
                .name(theater.getName())
                .location(theater.getLocation())
                .showtime(
                        Optional.ofNullable(theater.getShowtime())
                                .orElseGet(Collections::emptyList)
                                .stream()
                                .map(ShowtimeMappingHelper::map)
                                .toList())
                .capacity(theater.getCapacity())
                .isDeleted(theater.getIsDeleted())
                .createdAt(theater.getCreatedAt())
                .updatedAt(theater.getUpdatedAt())
                .build();
    }
}
