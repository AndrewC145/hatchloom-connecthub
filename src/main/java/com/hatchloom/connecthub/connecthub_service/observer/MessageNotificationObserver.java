package com.hatchloom.connecthub.connecthub_service.observer;

import com.hatchloom.connecthub.connecthub_service.builder.NotificationBuilder;
import com.hatchloom.connecthub.connecthub_service.enums.NotificationType;
import com.hatchloom.connecthub.connecthub_service.model.Messages;
import com.hatchloom.connecthub.connecthub_service.service.NotificationService;
import org.springframework.stereotype.Component;

@Component
public class MessageNotificationObserver implements MessageObserver {
    private final NotificationService notificationService;

    public MessageNotificationObserver(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Method is only used for 1-on-1 messages, no group messages yet
    @Override
    public void update(Messages message, Integer receiverUserId) {
        notificationService.createNotification(new NotificationBuilder()
                .setRecipientUserId(receiverUserId)
                .setSenderUserId(message.getSenderId())
                .setType(NotificationType.MESSAGE)
                .setMessage("You received a new message")
                .setConversationId(message.getConversationId())
                .setMessageId(message.getId()));
    }
}
