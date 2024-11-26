package com.uber.uberapi.services.locationtracking;

import com.uber.uberapi.models.Driver;
import com.uber.uberapi.models.ExactLocation;
import com.uber.uberapi.repositories.DriverRepository;
import com.uber.uberapi.services.Constants;
import com.uber.uberapi.services.messagequeue.MQMessage;
import com.uber.uberapi.services.messagequeue.MessageQueue;
import com.uber.uberapi.utils.quadtree.QuadTree;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

// deployed as a microservice
// consumes the location updates from a queue
// service (behind a REST API perhaps) that can provide drivers near a location

@Service
public class LocationTrackingService {
    @Autowired
    MessageQueue messageQueue;
    @Autowired
    Constants constants;
    @Autowired
    DriverRepository driverRepository;

    QuadTree world = new QuadTree();

    public List<Driver> getDriversNearLocation(ExactLocation pickup) {
        var list = world.findNeighboursIds(pickup.getLatitude(),
                pickup.getLongitude(),
                constants.getMaxDistanceKmForDriverMatching());

        return list.stream()
                .map(driverId -> driverRepository.findById(driverId).orElseThrow())
                 .toList();

    }

    public void updateDriverLocation(Driver driver, ExactLocation location) {
        world.removeNeighbour(driver.getId()); // if the driver is not in the world, it won't throw an error
        world.addNeighbour(driver.getId(), location.getLatitude(), location.getLongitude());
        driver.setLastKnownLocation(location);
        driverRepository.save(driver);
    }

    @Scheduled(fixedRate = 6000)
      //@KafkaListener(topics = "#{constants.getLocationTrackingTopicName()}")

    public void consumer() {

           //  MQMessage m = messageQueue.consumeMessage(constants.getDriverMatchingTopicName());
      /*  if (m == null){
            System.out.println("LocationTrackingService.consumer");
            return;
        }
        Message message = (Message) m;*/
        for (Driver d : driverRepository.findAll()) {

            updateDriverLocation(d, d.getLastKnownLocation());
        }
    }

    @Data
    @AllArgsConstructor
    public static class Message implements MQMessage {
        private Driver driver;
        private ExactLocation location;
    }
}
