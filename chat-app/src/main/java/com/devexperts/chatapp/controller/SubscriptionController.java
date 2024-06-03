package com.devexperts.chatapp.controller;

import com.devexperts.chatapp.service.SubscriptionService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/subscriptions")
    public String subscription(Model model, Principal principal) {
        String user = principal.getName();

        boolean isSubscribedUsd = subscriptionService.isSubscribed(user, "USD");
        model.addAttribute("isSubscribedUsd", isSubscribedUsd);

        boolean isSubscribedEur = subscriptionService.isSubscribed(user, "EUR");
        model.addAttribute("isSubscribedEur", isSubscribedEur);
        return "subscriptionForm";
    }

    @MessageMapping("/subscribe")
    public void subscribe(@Payload String symbol, Principal principal) {
        subscriptionService.subscribe(principal.getName(), symbol);
    }

    @MessageMapping("/unsubscribe")
    public void unsubscribe(@Payload String symbol, Principal principal) {
        subscriptionService.unsubscribe(principal.getName(), symbol);
    }
}

