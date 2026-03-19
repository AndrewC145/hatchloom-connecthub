package com.hatchloom.connecthub.connecthub_service.builder;

import com.hatchloom.connecthub.connecthub_service.enums.NotificationType;
import com.hatchloom.connecthub.connecthub_service.model.Notification;

public class NotificationBuilder implements Builder {
    private Integer recipientUserId;
    private Integer senderUserId;
    private NotificationType type;
    private String message;
    private Integer classifiedPostId;
    private Integer conversationId;
    private Integer messageId;

    @Override
    public NotificationBuilder setRecipientUserId(Integer recipientUserId) {
        this.recipientUserId = recipientUserId;
        return this;
    }

    @Override
    public NotificationBuilder setSenderUserId(Integer senderId) {
        this.senderUserId = senderId;
        return this;
    }

    @Override
    public NotificationBuilder setType(NotificationType type) {
        this.type = type;
        return this;
    }

    @Override
    public NotificationBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public NotificationBuilder setClassifiedPostId(Integer classifiedPostId) {
        this.classifiedPostId = classifiedPostId;
        return this;
    }

    @Override
    public NotificationBuilder setConversationId(Integer conversationId) {
        this.conversationId = conversationId;
        return this;
    }

    @Override
    public NotificationBuilder setMessageId(Integer messageId) {
        this.messageId = messageId;
        return this;
    }

    @Override
    public Notification build() {
        if (recipientUserId == null || senderUserId == null || type == null || message == null) {
            throw new IllegalStateException("RecipientUserId, SenderUserId, Type, and Message are required fields.");
        }

        Notification notification = new Notification();
        notification.setRecipientUserId(recipientUserId);
        notification.setSenderUserId(senderUserId);
        notification.setType(type);
        notification.setMessage(message);
        notification.setClassifiedPostId(classifiedPostId);
        notification.setConversationId(conversationId);
        notification.setMessageId(messageId);
        return notification;
    }
}
