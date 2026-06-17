package com.sabadin.taskspringapp.kafka;

import com.sabadin.lib.UserAuthEvent;
import com.sabadin.lib.constant.TopicsConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    public String bootstrapServers;

    @Value("${spring.kafka.producer.key-serializer}")
    public String keySerializer;

    @Value("${spring.kafka.producer.value-serializer}")
    public String valueSerializer;

    @Value("${spring.kafka.producer.acks}")
    public String acks;

    @Value("${spring.kafka.producer.properties.enable.idempotence}")
    public String idempotence;

    @Value("${spring.kafka.producer.properties.delivery.timeout.ms}")
    public String deliveryTimeout;

    @Value("${spring.kafka.producer.properties.linger.ms}")
    public String linger;

    @Value("${spring.kafka.producer.properties.request.timeout.ms}")
    public String requestTimeout;


    Map<String, Object> producerProps() {
        log.info("Filling props for producer...");
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        props.put(ProducerConfig.ACKS_CONFIG, acks);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, idempotence);
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeout);
        props.put(ProducerConfig.LINGER_MS_CONFIG, linger);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeout);
        log.info("Props for producer have been filled");
        displayProducerProps(props);
        return props;
    }

    private void displayProducerProps(Map<String, Object> props) {
        for (Map.Entry<String, Object> prop : props.entrySet()) {
            log.info(prop.getKey() + " = " + prop.getValue().toString() + ";");
        }
    }

    @Bean
    ProducerFactory<String, UserAuthEvent> producerFactory() {
        log.info("Creating Producer Factory...");
        return new DefaultKafkaProducerFactory<>(producerProps());
    }

    @Bean
    KafkaTemplate<String, UserAuthEvent> kafkaTemplate() {
        log.info("Creating Kafka Template...");
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    NewTopic createUserRegistrationTopic() {
        return TopicBuilder
                .name(TopicsConstants.USER_REGISTRATION_EVENTS_TOPIC)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }

    @Bean
    NewTopic createUserLoginTopic() {
        return TopicBuilder
                .name(TopicsConstants.USER_AUTH_EVENT_TOPIC)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }
}
