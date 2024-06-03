package com.devexperts.chatapp.controller;

import com.devexperts.chatapp.model.dto.UserViewAndEditDto;
import com.devexperts.chatapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserStatusController {

    private final UserService userService;

    public UserStatusController(UserService userService) {
        this.userService = userService;
    }

    @MessageMapping("/user.addUser")
    @SendTo("/topic/public")
    public String addUser(
            @Payload String username
    ) {
        userService.connect(username);
        return username;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/topic/public")
    public String disconnectUser(
            @Payload String username
    ) {
        userService.disconnect(username);
        return username;
    }


    @SecurityRequirement(name = "jwtToken")
    @Operation(summary = "View all users that are ONLINE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Online users successfully previewed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserViewAndEditDto.class))),
            @ApiResponse(responseCode = "403", description = "Unauthenticated", content = @Content(mediaType = "text/html")),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "text/html")),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(mediaType = "text/html"))})
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/users")
    public ResponseEntity<List<UserViewAndEditDto>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findConnectedUsers());
    }
}
