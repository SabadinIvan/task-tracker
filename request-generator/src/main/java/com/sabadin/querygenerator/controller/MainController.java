package com.sabadin.querygenerator.controller;

import com.sabadin.querygenerator.service.UserGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/generator")
@AllArgsConstructor
public class MainController {

    private UserGeneratorService userGeneratorService;

    @GetMapping
    public String testAPI() {
        return "Hello, I am here!";
    }

    @GetMapping("/user")
    public String generateUser() {
        return userGeneratorService.generateUser();
    }

    @GetMapping("/users/{count}")
    public List<String> generateUsers(@PathVariable int count) {
        return userGeneratorService.generateUsers(count);
    }
}
