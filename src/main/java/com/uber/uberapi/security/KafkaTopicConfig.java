package com.uber.uberapi.security;

import com.uber.uberapi.services.SchedulingService;
import com.uber.uberapi.services.messagequeue.DummyMessageMQImpl;
import com.uber.uberapi.services.messagequeue.MQMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

    private final KafkaTemplate<String, MQMessage> kafkaTemplate;

    @Scheduled(fixedRate = 5000)
    public void sendMessage() {
        MQMessage message = new DummyMessageMQImpl("hello");
        String topic = "demo";
        System.out.printf("Kafka: Sending to topic %s: %s%n", topic, message.toString());
        kafkaTemplate.send(topic, message);
    }
}

