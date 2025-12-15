package com.dawn.booking.service.Impl;

import com.dawn.booking.dto.response.SeatDTO;
import com.dawn.booking.service.SeatClientService;
import com.dawn.common.constant.Message;
import com.dawn.common.dto.response.ResponseObject;
import com.dawn.common.exception.wrapper.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@Component
public class SeatClientServiceImpl implements SeatClientService {

    private final RestClient restClient;

    public SeatClientServiceImpl(
            @Qualifier("baseRestClient") RestClient.Builder builder,
            @Value("${service.url}") String url) {
        this.restClient = builder.baseUrl(url).build();
    }

    @Override
    public List<SeatDTO> findByIdWithLock(List<Long> seatIds) {
        ResponseObject<List<SeatDTO>> response = restClient
                .get()
                .uri("/seats/locks", seatIds)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new ResourceNotFoundException(Message.Exception.MOVIE_NOT_FOUND);
                })
                .body(new ParameterizedTypeReference<>() {
                });
        if (response != null && response.getBody().getData() != null) {
            return response.getBody().getData();
        }
        return Collections.emptyList();
    }

    @Override
    public List<SeatDTO> findAllById(List<Long> seatIds) {
        ResponseObject<List<SeatDTO>> response = restClient
                .get()
                .uri("/seats/all/id", seatIds)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new ResourceNotFoundException(Message.Exception.MOVIE_NOT_FOUND);
                })
                .body(new ParameterizedTypeReference<>() {
                });

        if (response != null && response.getBody().getData() != null) {
            return response.getBody().getData();
        }
        return Collections.emptyList();
    }

    @Override
    public List<SeatDTO> findAllByReservationId(String reservationId) {
        ResponseObject<List<SeatDTO>> response = restClient
                .get()
                .uri("/seats/reservation/{reservationId}", reservationId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new ResourceNotFoundException(Message.Exception.MOVIE_NOT_FOUND);
                })
                .body(new ParameterizedTypeReference<>() {
                });

        if (response != null && response.getBody().getData() != null) {
            return response.getBody().getData();
        }
        return Collections.emptyList();
    }

    @Override
    public void saveAllSeat(List<SeatDTO> seats) {
        restClient
                .post()
                .uri("/seats/saveAll")
                .body(seats)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new ResourceNotFoundException(Message.Exception.MOVIE_NOT_FOUND);
                })
                .toBodilessEntity();
    }
}
