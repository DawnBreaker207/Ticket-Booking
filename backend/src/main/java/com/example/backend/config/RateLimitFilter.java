package com.example.backend.config;

import com.example.backend.config.response.ResponseObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@NoArgsConstructor
public class RateLimitFilter implements Filter {

    private final Map<String, AtomicInteger> requestCountsPerIpAddress = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    private static final int MAX_REQUESTS_PER_MINUTE = 50;
    private static final int TOO_MANY_REQUESTS = 429;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String clientIpAddress = req.getRemoteAddr();

//        Initialize request count for the client IP address
        requestCountsPerIpAddress.putIfAbsent(clientIpAddress, new AtomicInteger(0));
        AtomicInteger requestCount = requestCountsPerIpAddress.get(clientIpAddress);

//        Increment the request count
        int requests = requestCount.incrementAndGet();

//        Check if the request limit has been exceeded
        if (requests > MAX_REQUESTS_PER_MINUTE) {
            res.setStatus(TOO_MANY_REQUESTS);
            response.setContentType("application/json;charset=UTF-8");

            ResponseObject<Void> resObj = new ResponseObject<>(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Too many requests. Please try again later.",
                    null
            );
            res.getWriter().write(objectMapper.writeValueAsString(resObj));
            return;
        }

//        Allow the request to proceed
        chain.doFilter(request, response);
//        Optional: Reset request counts periodically (not implemented in this simple example)
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
