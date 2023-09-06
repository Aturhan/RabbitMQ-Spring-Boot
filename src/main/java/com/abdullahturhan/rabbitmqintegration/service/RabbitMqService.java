package com.abdullahturhan.rabbitmqintegration.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqService {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.routing.json.key}")
    public String key;
    @Value("${rabbitmq.exchange.name}")
    public String exchange;

    public RabbitMqService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message){
        rabbitTemplate.convertAndSend(exchange,key,message);
    }
}
