package com.uber.uberapi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private ConcurrentHashMap<String, ConcurrentHashMap<String, WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, WebSocketSession> getSessionsForBooking(String bookingId) {
        return roomSessions.getOrDefault(bookingId, new ConcurrentHashMap<>());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String bookingId = getBookingIdFromSession(session);
        if (bookingId != null) {
            roomSessions.putIfAbsent(bookingId, new ConcurrentHashMap<>());
            roomSessions.get(bookingId).put(session.getId(), session);
            log.info("New room session created {}", bookingId + " " + session.getId());
        }
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String bookingId = getBookingIdFromSession(session);
        if (bookingId != null && roomSessions.containsKey(bookingId)) {
            for (WebSocketSession s : roomSessions.get(bookingId).values()) {
                if (s.isOpen() && !s.getId().equals(session.getId())) {
                    s.sendMessage(new TextMessage(message.getPayload()));
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        String bookingId = getBookingIdFromSession(session);
        if (bookingId != null && roomSessions.containsKey(bookingId)) {
            roomSessions.get(bookingId).remove(session.getId());
            if (roomSessions.get(bookingId).isEmpty()) {
                roomSessions.remove(bookingId);
            }
        }
        System.out.println("Connection closed for session: " + session.getId());
    }

    // Generate a session ID based on booking ID
    private String getBookingIdFromSession(WebSocketSession session) {
        if (session.getUri() == null || session.getUri().getQuery() == null) {
            throw new IllegalArgumentException("WebSocket connection is missing the bookingId query parameter.");
        }
        String[] queryParams = session.getUri().getQuery().split("=");
        if (queryParams.length < 2 || !"bookingId".equals(queryParams[0])) {
            throw new IllegalArgumentException("Invalid or missing bookingId in query parameters.");
        }
        return queryParams[1];
    }

    public static String generateSessionId(String bookingId) {
        return bookingId + "-" + UUID.randomUUID().toString();
    }
}
