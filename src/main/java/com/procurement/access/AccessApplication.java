package com.procurement.access;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AccessApplication {

    public static void main(final String[] args) {
        SpringApplication.run(AccessApplication.class, args);
    }
}
