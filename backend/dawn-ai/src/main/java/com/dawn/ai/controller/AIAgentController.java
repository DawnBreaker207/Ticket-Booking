package com.dawn.ai.controller;

import com.dawn.ai.service.AiAgentProvider;
import com.dawn.common.dto.response.ResponseObject;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AIAgentController {

    private final AiAgentProvider aiAgentProvider;

    public ResponseObject<String> chat(@RequestParam String question) {
        return ResponseObject.success(aiAgentProvider.ask(question));
    }
}
