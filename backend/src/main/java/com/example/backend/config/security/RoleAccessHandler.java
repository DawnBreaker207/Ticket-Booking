package com.example.backend.config.security;

import com.example.backend.config.response.ResponseObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RoleAccessHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    public RoleAccessHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");


        ResponseObject<Void> resObj = new ResponseObject<>(
                HttpStatus.FORBIDDEN,
                "You don't have permission",
                null
        );
        response.getWriter().write(objectMapper.writeValueAsString(resObj.getBody()));
    }
}
