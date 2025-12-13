package com.dawn.api.cinema.service;

import com.dawn.api.cinema.dto.ShowtimeDTO;

public interface ShowtimeClientService {
    ShowtimeDTO findById(Long id);

    ShowtimeDTO save(ShowtimeDTO showtime);
}
