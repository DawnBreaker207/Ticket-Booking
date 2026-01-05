package com.dawn.ai.service;

import dev.langchain4j.service.spring.AiService;

@AiService
public interface AiAgentService {
    String chat(String message);
}
