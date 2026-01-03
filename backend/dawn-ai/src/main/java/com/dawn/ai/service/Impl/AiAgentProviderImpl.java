package com.dawn.ai.service.Impl;

import com.dawn.ai.service.AiAgentProvider;
import com.dawn.ai.service.AiAgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiAgentProviderImpl implements AiAgentProvider {

    private final AiAgentService aiAgentService;

    @Override
    public String ask(String question) {
        return aiAgentService.chat(question);
    }

    public String askAdmin(String question) {
        if(question == null || question.isBlank()){
            return "Vui lòng nhập câu hỏi";
        }
        return aiAgentService.chat(question);
    }

}
