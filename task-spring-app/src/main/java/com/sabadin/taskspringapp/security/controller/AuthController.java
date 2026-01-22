package com.sabadin.taskspringapp.security.controller;

import com.sabadin.taskspringapp.security.model.dto.LoginRequestDto;
import com.sabadin.taskspringapp.security.model.dto.SignupRequestDto;
import com.sabadin.taskspringapp.security.model.dto.UserAuthResponse;
import com.sabadin.taskspringapp.security.service.AuthService;
import com.sabadin.taskspringapp.security.service.UserDetailsServiceImpl;
import com.sabadin.taskspringapp.security.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
//@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> userSignup(@RequestBody SignupRequestDto request) {
        log.info("called AuthController -> userSignup; user logon name -> {}", request.getLogonName());
        try {
            if (userDetailsService.iExistsByLogonName(request.getLogonName())) {
                return ResponseEntity.badRequest().body("LogonName is already taken");
            }
            if (userService.isExistEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body("Email is already taken");
            }
            authService.userSignup(request);
            log.info("User {} has been registered", request.getLogonName());
            return ResponseEntity.ok("User signed up successfully");
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body("Failed to register User");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register User");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthResponse> userLogin(@RequestBody LoginRequestDto request) {
        log.info("called AuthController -> userLogin");
        UserAuthResponse response = authService.userLogin(request);
        log.info("response -> " + response);
        return ResponseEntity.ok(response);
    }
}
