package com.example.stravaclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication(scanBasePackages = "com.example.stravaclient")

public class StravaClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(StravaClientApplication.class, args);
    }

}
