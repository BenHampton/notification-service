package com.order.notification_service.service;

import com.order.notification_service.model.Test;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestService {

    private final PublisherService publisherService;

    public TestService(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    public Test publishTest(Long id, String name) {

        Test test = Test.builder()
                .id(id)
                .name(name)
                .build();

        log.info("Publishing Test: " + test);
        publisherService.publishTest(test);

        return test;
    }
}
