package com.devexperts.chatapp.model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "chat_rooms")
public class ChatRoomEntity extends BaseEntity {

    private String chatId;
    private String senderId;
    private String recipientId;

    public ChatRoomEntity() {
    }

    public String getChatId() {
        return chatId;
    }

    public ChatRoomEntity setChatId(String chatId) {
        this.chatId = chatId;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public ChatRoomEntity setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public ChatRoomEntity setRecipientId(String recipientId) {
        this.recipientId = recipientId;
        return this;
    }
}
