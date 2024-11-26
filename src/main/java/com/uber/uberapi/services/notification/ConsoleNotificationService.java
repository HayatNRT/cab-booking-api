package com.uber.uberapi.services.notification;

import com.uber.uberapi.config.ChatWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
@RequiredArgsConstructor
public class ConsoleNotificationService implements NotificationService {
    private final ChatWebSocketHandler chatWebSocketHandler;

    @Override
    public void notify(String phoneNumber, String message) {
        //   System.out.println("Notification for  " + phoneNumber + " " + message);
       // chatWebSocketHandler.getSessionsForBooking()
    }
}

// doing dev on local machine
// deploy to prod, you will use different notification