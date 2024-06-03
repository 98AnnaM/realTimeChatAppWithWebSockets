package com.devexperts.aapl_symbol_service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock")
public class MockDataController {

    @GetMapping("/data/USD")
    public ResponseEntity<SymbolDto> getMockDataUSD() {
        double price = 1 + Math.random();
        SymbolDto mockData = new SymbolDto("USD", price);
        return ResponseEntity.ok(mockData);
    }

    @GetMapping("/data/EUR")
    public ResponseEntity<SymbolDto> getMockDataEUR() {
        double price = 1 + Math.random();
        SymbolDto mockData = new SymbolDto("EUR", price);
        return ResponseEntity.ok(mockData);
    }

}
