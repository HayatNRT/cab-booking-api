package com.uber.uberapi.services.notification;

import com.uber.uberapi.config.ChatWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsoleNotificationService implements NotificationService {
    private final ChatWebSocketHandler webSocketHandler;

    @Override
    public void notify(String phoneNumber, String message) {
     webSocketHandler.sendMessageToRoom(phoneNumber, message);

        //   System.out.println("Notification for  " + phoneNumber + " " + message);
    }
}

// doing dev on local machine
// deploy to prod, you will use different notification