package com.dawn.cinema.helper;

import com.dawn.cinema.dto.request.TheaterRequest;
import com.dawn.cinema.dto.response.TheaterResponse;
import com.dawn.cinema.model.Theater;

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
                .capacity(theater.getCapacity())
                .isDeleted(theater.getIsDeleted())
                .createdAt(theater.getCreatedAt())
                .updatedAt(theater.getUpdatedAt())
                .build();
    }
}
