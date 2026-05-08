package com.sabadin.logservice.handler;

import com.sabadin.lib.UserAuthEvent;
import com.sabadin.lib.constant.TopicsConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@KafkaListener(topics = TopicsConstants.USER_REGISTRATION_EVENTS_TOPIC)
public class UserAuthHandler {

    @KafkaHandler
    public void userCreateHandler(UserAuthEvent event) {
        log.info("Received event; email -> " + event.getEmail());
        log.info("Imitation of log to file. Need to do implementation this feature");
        // ToDo implementation to log to real file
    }
}
