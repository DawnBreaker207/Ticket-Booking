package com.dawn.ai.controller;

import com.dawn.ai.service.AiAgentProvider;
import com.dawn.common.core.dto.response.ResponseObject;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIAgentController {

    private final AiAgentProvider aiAgentProvider;

    @PostMapping("/chat")
    public ResponseObject<String> chat(@RequestBody ChatRequest question) {
        String answer = aiAgentProvider.ask(question.getMessage());
        return ResponseObject.success(answer);
    }
}
