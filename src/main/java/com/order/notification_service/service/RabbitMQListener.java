package com.order.notification_service.service;

import com.order.notification_service.config.RabbitConfigProperties;
import com.order.notification_service.model.Test;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQListener {

    private final RabbitConfigProperties rabbitConfigProperties;

    public RabbitMQListener(RabbitConfigProperties rabbitConfigProperties) {
        this.rabbitConfigProperties = rabbitConfigProperties;
    }


    @RabbitListener(queues = {"test-queue"})
    public void testListenerEvent(Test event) {
        log.info("Test Event Received: {}", event);
    }
}
