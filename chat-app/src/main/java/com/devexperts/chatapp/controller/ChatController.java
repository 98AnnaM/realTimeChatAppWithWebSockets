package com.devexperts.chatapp.controller;

import com.devexperts.chatapp.model.dto.ChatNotification;
import com.devexperts.chatapp.model.entity.MessageEntity;
import com.devexperts.chatapp.service.JwtService;
import com.devexperts.chatapp.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService chatMessageService;
    private final JwtService jwtService;

    public ChatController(SimpMessagingTemplate messagingTemplate, MessageService chatMessageService, JwtService jwtService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
        this.jwtService = jwtService;
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload MessageEntity chatMessage) {
        MessageEntity savedMsg = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSenderId(),
                        savedMsg.getRecipientId(),
                        savedMsg.getContent()
                )
        );
    }

    @SecurityRequirement(name = "jwtToken")
    @Operation(summary = "Get the chat messages between two users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got chat messages.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageEntity.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not permitted operation",
                    content = @Content(mediaType = "text/html")),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "text/html")),
            @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(mediaType = "text/html"))
    })
    @PreAuthorize("(isAuthenticated() && (#senderId == authentication.principal.username or #recipientId == authentication.principal.username))")
    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<MessageEntity>> findChatMessages(@PathVariable String senderId,
                                                                @PathVariable String recipientId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

    @GetMapping("/startChat")
    public String showChatRooms(HttpServletRequest request, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUsername", authentication.getName());
        return "chat";
    }
}

