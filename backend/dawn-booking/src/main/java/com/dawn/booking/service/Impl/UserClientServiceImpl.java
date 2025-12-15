package com.dawn.booking.service.Impl;

import com.dawn.booking.dto.response.RoleDTO;
import com.dawn.booking.dto.response.UserDTO;
import com.dawn.booking.service.UserClientService;
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
public class UserClientServiceImpl implements UserClientService {

    private final RestClient restClient;

    public UserClientServiceImpl(
            @Qualifier("baseRestClient") RestClient.Builder builder,
            @Value("${service.url}") String url) {
        this.restClient = builder.baseUrl(url).build();
    }


    @Override
    public boolean existsByRolesName(String roleName) {
        return Boolean.TRUE.equals(restClient
                .get()
                .uri("/user/role/existed/{roleName}", roleName)
                .retrieve().onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new ResourceNotFoundException(Message.Exception.MOVIE_NOT_FOUND);
                })
                .body(Boolean.class));
    }

    @Override
    public RoleDTO findByRoleName(String roleName) {
        ResponseObject<RoleDTO> response = restClient
                .get()
                .uri("/user/role/{roleName}", roleName)
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

    @Override
    public UserDTO findWithEmail(String email) {
        ResponseObject<UserDTO> response = restClient
                .get()
                .uri("/user/email/{email}", email)
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

    @Override
    public UserDTO findById(Long id) {
        ResponseObject<UserDTO> response = restClient
                .get()
                .uri("/user/{id}", id)
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
