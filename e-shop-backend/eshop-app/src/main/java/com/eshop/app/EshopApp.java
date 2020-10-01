package com.eshop.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.eshop")
@EnableJpaRepositories("com.eshop.repositories")
@EntityScan("com.eshop.models")
public class EshopApp {
    public static void main(String[] args) {
        SpringApplication.run(EshopApp.class, args);
    }
}
