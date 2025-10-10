package com.example.backend.controller;

import com.example.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/test")
    public void sendEmail() {
        notificationService.sendEmail("ngotunganh207@gmail.com", "test", "ORD-D4DAAD940C10");
    }

}
