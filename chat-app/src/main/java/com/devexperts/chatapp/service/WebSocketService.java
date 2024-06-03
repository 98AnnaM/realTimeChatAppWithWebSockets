package com.devexperts.chatapp.service;

import com.devexperts.chatapp.model.dto.SymbolDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(String username, SymbolDto message) {
        String destination = "/user/" + username + "/mock/messages";
        logger.info("Sending message to user: {}, destination: {}, message: {}", username, destination, message);
        messagingTemplate.convertAndSend(destination, message);
    }
}
