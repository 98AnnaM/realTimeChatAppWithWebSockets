package com.devexperts.chatapp.model.dto;

public class QuoteDto {

  private String content;
  private String author;

  public QuoteDto() {
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }
}
