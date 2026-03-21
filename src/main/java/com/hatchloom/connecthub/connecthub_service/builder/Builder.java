package com.hatchloom.connecthub.connecthub_service.builder;

import com.hatchloom.connecthub.connecthub_service.enums.NotificationType;
import com.hatchloom.connecthub.connecthub_service.model.Notification;

/**
 * Builder design pattern interface for creating Notification objects
 */
public interface Builder {
    Builder setRecipientUserId(Integer recipientId);
    Builder setSenderUserId(Integer senderId);
    Builder setType(NotificationType type);
    Builder setMessage(String message);
    Builder setClassifiedPostId(Integer classifiedPostId);
    Builder setConversationId(Integer conversationId);
    Builder setMessageId(Integer messageId);
    Notification build();
}
