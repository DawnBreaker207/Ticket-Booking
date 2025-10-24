package com.example.backend.controller;

import com.example.backend.helper.RedisKeyHelper;
import com.example.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/mail/test")
    public void sendEmail() {
        notificationService.sendEmail(
                "ngotunganh207@gmail.com",
                "Ngo Tung Anh",
                "ORD-D4DAAD940C10",
                "Overlord",
                "Thanh Xuan",
                "11/10/2025 12:00:00",
                "A2,A3",
                "11/10/2025 12:00:00",
                "100000"
        );
    }

    @GetMapping(value = "/subscribe/showtime/{showtimeId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@PathVariable Long showtimeId, @RequestParam(defaultValue = "anonymous") String clientId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CACHE_CONTROL, "no-store");
        headers.add(HttpHeaders.CONNECTION, "keep-alive");
        SseEmitter emitter =  notificationService.subscribe(RedisKeyHelper
                .showtimeChannel(showtimeId), clientId);
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(emitter);
    }

}
