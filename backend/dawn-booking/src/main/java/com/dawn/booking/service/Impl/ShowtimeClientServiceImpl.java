package com.dawn.booking.service.Impl;

import com.dawn.booking.dto.response.ShowtimeDTO;
import com.dawn.booking.service.ShowtimeClientService;
import com.dawn.common.constant.Message;
import com.dawn.common.exception.wrapper.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class ShowtimeClientServiceImpl implements ShowtimeClientService {

    private final RestClient restClient;

    public ShowtimeClientServiceImpl(
            @Qualifier("baseRestClient") RestClient.Builder builder,
            @Value("${service.url}") String url) {
        this.restClient = builder.baseUrl(url).build();
    }

    @Override
    public ShowtimeDTO findById(Long id) {
        return restClient
                .get()
                .uri("/showtime/{id}", id)
                .retrieve().onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new ResourceNotFoundException(Message.Exception.MOVIE_NOT_FOUND);
                })
                .body(ShowtimeDTO.class);
    }

    @Override
    public ShowtimeDTO save(ShowtimeDTO showtime) {
        return restClient
                .post()
                .uri("/showtime", showtime)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new ResourceNotFoundException(Message.Exception.MOVIE_NOT_FOUND);
                })
                .body(ShowtimeDTO.class);
    }
}
