package com.order.notification_service.controller;

import com.order.notification_service.model.Test;
import com.order.notification_service.service.TestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("test")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<Test> test(@PathVariable("id") Long id, @RequestParam("name") String name) {
        return ResponseEntity.ok(testService.publishTest(id, name));
    }
}
