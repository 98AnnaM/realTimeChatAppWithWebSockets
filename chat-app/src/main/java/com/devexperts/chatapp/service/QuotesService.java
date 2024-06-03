package com.devexperts.chatapp.service;

import com.devexperts.chatapp.model.dto.QuoteDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class QuotesService {

  private final WebClient webClient;

  public QuotesService(WebClient webClient) {
    this.webClient = webClient;
  }

  public QuoteDto getQuote() {
    return webClient.get().uri("/quote").retrieve().bodyToMono(QuoteDto.class).block();
  }
}
