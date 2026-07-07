package com.ogoma.marketing.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class ApiApplication {

    static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Bean
    ApplicationRunner runner(ApplicationContext context) {
        return args -> log.info("Context: {}", context.getClass().getName());
    }
}
