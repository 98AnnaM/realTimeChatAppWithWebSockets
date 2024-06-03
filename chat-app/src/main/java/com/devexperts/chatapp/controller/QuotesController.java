package com.devexperts.chatapp.controller;

import com.devexperts.chatapp.model.dto.QuoteDto;
import com.devexperts.chatapp.service.QuotesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuotesController {

  private final QuotesService quotesService;


  public QuotesController(QuotesService quotesService) {
    this.quotesService = quotesService;
  }

  @GetMapping("/quote")
  public String getRandomQuote(Model model) {
    QuoteDto quote = quotesService.getQuote();
    model.addAttribute("quoteText", quote.getContent());
    model.addAttribute("quoteAuthor", quote.getAuthor());
    return "quote";
  }

}
