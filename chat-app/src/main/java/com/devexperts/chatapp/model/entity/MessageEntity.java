package com.devexperts.chatapp.model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "messages")
public class MessageEntity extends BaseEntity {

    private String chatId;
    private String senderId;
    private String recipientId;
    private String content;
    private Date timestamp;

    public MessageEntity() {
    }

    public String getChatId() {
        return chatId;
    }

    public MessageEntity setChatId(String chatId) {
        this.chatId = chatId;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public MessageEntity setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public MessageEntity setRecipientId(String recipientId) {
        this.recipientId = recipientId;
        return this;
    }

    public String getContent() {
        return content;
    }

    public MessageEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public MessageEntity setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
