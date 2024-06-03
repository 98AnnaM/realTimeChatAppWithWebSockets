package com.devexperts.chatapp.model.dto;

public class ChatNotification {

    private Long id;
    private String senderId;
    private String recipientId;
    private String content;

    public ChatNotification() {
    }

    public ChatNotification(Long id, String senderId, String recipientId, String content) {
        this.id = id;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public ChatNotification setId(Long id) {
        this.id = id;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public ChatNotification setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public ChatNotification setRecipientId(String recipientId) {
        this.recipientId = recipientId;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ChatNotification setContent(String content) {
        this.content = content;
        return this;
    }
}
