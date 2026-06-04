package com.sabadin.querygenerator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QueryGeneratorApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(QueryGeneratorApplication.class, args);
    }

    @Value("${server.port}")
    public String serverPort;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("SPRING BOOT APP 'QueryGeneratorApplication' STARTED. Server Port: " + serverPort);
    }
}
