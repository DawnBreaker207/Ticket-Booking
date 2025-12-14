package com.dawn.cinema.service.Impl;

import com.dawn.cinema.dto.response.MovieDTO;
import com.dawn.cinema.service.MovieClientCinemaService;
import com.dawn.common.constant.Message;
import com.dawn.common.dto.response.ResponseObject;
import com.dawn.common.exception.wrapper.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
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
        ResponseObject<MovieDTO> response = restClient
                .get()
                .uri("/movie/{id}", id)
                .retrieve().onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new ResourceNotFoundException(Message.Exception.MOVIE_NOT_FOUND);
                })
                .body(new ParameterizedTypeReference<>() {
                });
        
        if (response != null && response.getBody().getData() != null) {
            return response.getBody().getData();
        }
        throw new ResourceNotFoundException(Message.Exception.MOVIE_NOT_FOUND);
    }
}
