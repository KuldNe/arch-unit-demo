package com.tietoevry.archunitdemo.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HealthCheckEndpoint {

    public static final String ALIVE = "alive";

    @GetMapping("/health-check")
    public String healthCheck() {
        return ALIVE;
    }

}
