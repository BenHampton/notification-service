package com.order.notification_service.service;

import com.order.notification_service.config.RabbitConfigProperties;
import com.order.notification_service.model.Test;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PublisherService {

    private final RabbitConfigProperties rabbitConfigProperties;

    private final RabbitTemplate rabbitTemplate;


    public PublisherService(RabbitConfigProperties rabbitConfigProperties, RabbitTemplate rabbitTemplate) {
        this.rabbitConfigProperties = rabbitConfigProperties;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Async("rabbitTaskExecutor")
    public void publishTest(Test test) {

        log.info("Test Message to publish: " + test);

        try {

            rabbitTemplate.convertAndSend(
                    rabbitConfigProperties.getTestQueueExchange(),
                    rabbitConfigProperties.getTestQueueRoutingKey(),
                    test);
            log.info("Sending message: " + test);

        } catch (AmqpException ex) {

            log.error("Error publishing message", ex);
        }
    }
}
