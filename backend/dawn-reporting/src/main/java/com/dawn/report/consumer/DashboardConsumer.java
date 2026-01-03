package com.dawn.report.consumer;

import com.dawn.common.constant.RabbitMQConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DashboardConsumer {
    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = RabbitMQConstants.QUEUE_DASHBOARD)
    public void handleUpdate(Object message) {
        Map<String, String> payload = Collections.singletonMap("action", "REFRESH");
        messagingTemplate.convertAndSend("/topic/dashboard/update", payload);
    }
}
