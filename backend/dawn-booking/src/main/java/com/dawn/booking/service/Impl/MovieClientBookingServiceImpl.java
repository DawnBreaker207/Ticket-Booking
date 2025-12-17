package com.dawn.booking.service.Impl;

import com.dawn.booking.dto.response.MovieDTO;
import com.dawn.booking.service.MovieClientBookingService;
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
public class MovieClientBookingServiceImpl implements MovieClientBookingService {

    private final RestClient restClient;

    public MovieClientBookingServiceImpl(
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

        if (response != null && response.getData() != null) {
            return response.getData();
        }
        throw new ResourceNotFoundException(Message.Exception.MOVIE_NOT_FOUND);
    }
}
