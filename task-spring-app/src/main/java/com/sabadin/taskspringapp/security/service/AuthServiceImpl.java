package com.sabadin.taskspringapp.security.service;

import com.sabadin.lib.UserAuthEvent;
import com.sabadin.taskspringapp.aspects.annotation.AuthLog;
import com.sabadin.lib.constant.TopicsConstants;
import com.sabadin.taskspringapp.aspects.annotation.LogExecutionTime;
import com.sabadin.taskspringapp.security.jwt.JwtService;
import com.sabadin.taskspringapp.security.model.dto.*;
import com.sabadin.taskspringapp.security.model.entity.User;
import com.sabadin.taskspringapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final KafkaTemplate<String, UserAuthEvent> kafkaTemplate;

    @AuthLog
    @LogExecutionTime
    public AuthResponse register(RegisterRequest request) {
        User savedUser = userService.createNewUser(request);
        UserAuthEvent userAuthEvent = new UserAuthEvent(savedUser.getId(), savedUser.getEmail(), savedUser.getFirstName(), savedUser.getLastName());
        sendMessageToBroker(TopicsConstants.USER_REGISTRATION_EVENTS_TOPIC, savedUser.getEmail(), userAuthEvent);
        var jwtToken = jwtService.generateToken(savedUser);
        return new AuthResponse(jwtToken);
    }

    @AuthLog(showArgs = false)
    @LogExecutionTime
    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogonName(),
                        request.getPassword()
                )
        );
        User user = (User) authentication.getPrincipal();
        UserAuthEvent userAuthEvent = new UserAuthEvent(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName());
        sendMessageToBroker(TopicsConstants.USER_AUTH_EVENT_TOPIC, user.getEmail(), userAuthEvent);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void sendMessageToBroker(String topic, String key, UserAuthEvent event) {
        CompletableFuture<SendResult<String, UserAuthEvent>> future = kafkaTemplate.send(topic, key, event);
        future.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("Failed to send message: {}", exception.getMessage());
            } else {
                log.info("Message sent successfully. Topic: {}; key: {}, partition: {}, offset: {}",
                        result.getRecordMetadata().topic(), key, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            }
        });
    }
}
