package com.devexperts.mockedservice;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class QuoteService {

    public QuoteDto getRandomQuote(){
        List<QuoteDto> quotes = getAllQuotes();
        int randomIndex = new Random().nextInt(quotes.size());
        return quotes.get(randomIndex);
    }

    private List<QuoteDto> getAllQuotes() {
        return Stream.of(QuoteEnum.values())
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private QuoteDto mapToDto(QuoteEnum quoteEnum) {
        QuoteDto dto = new QuoteDto();
        dto.setContent(quoteEnum.getContent());
        dto.setAuthor(quoteEnum.getAuthor());
        return dto;
    }
}
