package com.devexperts.mockedservice;

public enum QuoteEnum {
    STEVE_JOBS("The only way to do great work is to love what you do.", "Steve Jobs"),
    ABRAHAM_LINCOLN("In the end, it's not the years in your life that count. It's the life in your years.", "Abraham Lincoln"),
    WINSTON_CHURCHILL("Success is not final, failure is not fatal: It is the courage to continue that counts.", "Winston Churchill"),
    JOHN_LENNON("Life is what happens when you're busy making other plans.", "John Lennon"),
    FRANKLIN_ROOSEVELT("The only limit to our realization of tomorrow will be our doubts of today.", "Franklin D. Roosevelt"),
    THEODORE_ROOSEVELT("Believe you can and you're halfway there.", "Theodore Roosevelt"),
    NELSON_MANDELA("The greatest glory in living lies not in never falling, but in rising every time we fall.", "Nelson Mandela"),
    DALAI_LAMA("Happiness is not something readymade. It comes from your own actions.", "Dalai Lama"),
    ELEANOR_ROOSEVELT("The future belongs to those who believe in the beauty of their dreams.", "Eleanor Roosevelt"),
    WAYNE_GRETZKY("You miss 100% of the shots you don't take.", "Wayne Gretzky");

    private final String content;
    private final String author;

    QuoteEnum(String content, String author) {
        this.content = content;
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }
}
