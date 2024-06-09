package com.order.notification_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "rabbitmq")
@Configuration
@Data
public class RabbitConfigProperties {

    private String testExchange;

    private String testRoutingKey;

    private String testQueueRoutingKey;

    private String testQueueExchange;
}
