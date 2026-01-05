package com.dawn.ai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface AiAgentProvider {
    String ask(String question);

    @SystemMessage("Bạn là một trợ lý hỗ trợ quản trị chuyên nghiệp của hệ thống CinePlex.")
    public String askAdmin(@UserMessage String question);
}
