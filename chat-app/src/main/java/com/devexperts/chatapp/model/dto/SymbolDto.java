package com.devexperts.chatapp.model.dto;

public class SymbolDto {

    String symbol;
    Double price;

    public SymbolDto() {
    }

    public SymbolDto(String symbol, Double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
