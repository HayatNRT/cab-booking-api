package com.uber.uberapi.services.messagequeue;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class KafkaService implements MessageQueue {
    // Fake in-memory kafka
    // not thread safe
//    private final Map<String, Queue<MQMessage>> topics = new HashMap<>();
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendMessage(String topic, MQMessage message) {
//        topics.putIfAbsent(topic, new LinkedList<>());
//        topics.get(topic).add(message);
        kafkaTemplate.send(topic, message);
    }



//    @Override
//    public MQMessage consumeMessage(String topic) {
//        MQMessage message = topics.getOrDefault(topic, new LinkedList<>()).poll();
//        return message;
//    }

//        @Override
//    public MQMessage consumeMessage(String topic) {
//        MQMessage message = kafkaTemplate.
//        return message;
//    }
}
