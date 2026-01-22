package com.sabadin.taskspringapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskSpringAppApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(TaskSpringAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("SPRING BOOT APP 'TaskSpringAppApplication' STARTED.");
    }
}
