package com.uber.uberapi.config;

import com.uber.uberapi.models.Booking;
import com.uber.uberapi.repositories.BookingRepository;
import com.uber.uberapi.services.Constants;
import com.uber.uberapi.services.drivermatching.DriverMatchingService;
import com.uber.uberapi.services.messagequeue.MQMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

    private final KafkaTemplate<String, MQMessage> kafkaTemplate;

    private final BookingRepository bookingRepository;
    private final Constants constants;

//    @Scheduled(fixedRate = 30000)
//    @RetryableTopic(attempts = "4")
    public void sendMessage() {
        Booking booking = bookingRepository.findById(1L).get();
        MQMessage message = new DriverMatchingService.Message(booking);
        String topic = constants.getDriverMatchingTopicName();
      //  System.out.printf("Kafka: Sending to topic %s: %s%n", topic, message.toString());
        kafkaTemplate.send(topic, message);
    }

//    @KafkaListener(topics = "driverMatchingTopic",groupId = "schedulingServiceGroup")
//    public void consumer(MQMessage m) {
////        MQMessage m = messageQueue.consumeMessage(constants.getDriverMatchingTopicName());
//
//        if (m == null) return;
//        DriverMatchingService.Message message = (DriverMatchingService.Message) m;
//        System.out.println(message);
////        findNearbyDrivers(message.getBooking());
//    }
}

