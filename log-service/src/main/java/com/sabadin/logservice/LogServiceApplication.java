package com.sabadin.logservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogServiceApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(LogServiceApplication.class, args);
    }

    @Value("${server.port}")
    public String serverPort;

    @Override
    public void run(String... args) {
        System.out.println("SPRING BOOT APP 'LogServiceApplication' STARTED. Server Port: " + serverPort);
    }
}
