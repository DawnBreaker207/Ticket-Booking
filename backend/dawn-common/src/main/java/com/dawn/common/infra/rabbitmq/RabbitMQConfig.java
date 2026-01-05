package com.dawn.common.infra.rabbitmq;

import com.dawn.common.core.constant.RabbitMQConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue queueNotify() {
        return new Queue(RabbitMQConstants.QUEUE_NOTIFY);
    }

    @Bean
    public Queue queueDashboard(){
        return new Queue(RabbitMQConstants.QUEUE_DASHBOARD);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(RabbitMQConstants.EXCHANGE_NOTIFY);
    }

    @Bean
    public Binding bindingNotify(Queue queueNotify, TopicExchange exchange) {
        return BindingBuilder
                .bind(queueNotify)
                .to(exchange)
                .with(RabbitMQConstants.ROUTING_KEY_NOTIFY);
    }

    @Bean
    public Binding bindingDashboard(Queue queueDashboard, TopicExchange exchange) {
        return BindingBuilder
                .bind(queueDashboard)
                .to(exchange)
                .with(RabbitMQConstants.ROUTING_KEY_DASHBOARD);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
