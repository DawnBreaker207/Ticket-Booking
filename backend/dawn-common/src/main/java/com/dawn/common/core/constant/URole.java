package com.dawn.common.core.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum URole {
    USER(1),
    MODERATOR(2),
    ADMIN(3);

    private final int level;
}
