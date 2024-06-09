package com.order.notification_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.retry.support.RetryTemplateBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.send.retry.max-attempts}")
    int maxAttempts;

    @Value("${rabbitmq.send.retry.initial-interval}")
    long initialInterval;

    @Value("${rabbitmq.send.retry.max-interval}")
    int maxInterval;

    @Value("${rabbitmq.send.retry.multiplier}")
    double multiplier;

    @Value("${rabbitmq.thread.core-pool-size}")
    int corePoolSize;

    @Value("${rabbitmq.thread.max-pool-size}")
    int maxPoolSize;

    @Value("${rabbitmq.thread.queue-capacity}")
    int queueCapacity;

    private final CachingConnectionFactory cachingConnectionFactory;

    private final RabbitConfigProperties rabbitConfigProperties;

    public RabbitMQConfig(CachingConnectionFactory cachingConnectionFactory,
                          RabbitConfigProperties rabbitConfigProperties) {
        this.cachingConnectionFactory = cachingConnectionFactory;
        this.rabbitConfigProperties = rabbitConfigProperties;
    }

    @Bean
    public FanoutExchange jobsiteExchange() {
        // Will create exchange if it doesn't exist
        return ExchangeBuilder
                .fanoutExchange(rabbitConfigProperties.getJobsiteExchange())
                .durable(true)
                .build();
    }


    @Bean
    public Queue testQueue() {
        return QueueBuilder.durable(rabbitConfigProperties.getTestRoutingKey()).build();
    }

    @Bean
    public Binding testBinding() {
        return BindingBuilder
                .bind(testQueue())
                .to(testTopicExchange())
                .with(rabbitConfigProperties.getTestRoutingKey());
    }

    @Bean
    public TopicExchange testTopicExchange() {
        return ExchangeBuilder
                .topicExchange(rabbitConfigProperties.getTestExchange())
                .durable(true)
                .build();
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(JacksonConfig jacksonConfig) {
        return new Jackson2JsonMessageConverter(jacksonConfig.jackson2ObjectMapper());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);

        rabbitTemplate.setRetryTemplate(new RetryTemplateBuilder()
                .maxAttempts(maxAttempts).exponentialBackoff(initialInterval, multiplier, maxInterval).build());
        return rabbitTemplate;
    }

    @Bean
    public TaskExecutor rabbitTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.initialize();
        return executor;
    }

}
