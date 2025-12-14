package com.dawn.cinema.service.Impl;

import com.dawn.cinema.dto.response.MovieDTO;
import com.dawn.cinema.service.MovieClientCinemaService;
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
public class MovieClientCinemaServiceImpl implements MovieClientCinemaService {

    private final RestClient restClient;

    public MovieClientCinemaServiceImpl(
            @Qualifier("baseRestClient") RestClient.Builder builder,
            @Value("${service.url}") String url) {
        this.restClient = builder.baseUrl(url).build();
    }

    @Override
    public MovieDTO findOne(Long id) {
        return restClient
                .get()
                .uri("/movie/{id}", id)
                .retrieve().onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new ResourceNotFoundException(Message.Exception.MOVIE_NOT_FOUND);
                })
                .body(MovieDTO.class);
    }
}
