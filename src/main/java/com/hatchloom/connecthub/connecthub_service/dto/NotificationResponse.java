package com.hatchloom.connecthub.connecthub_service.dto;

import com.hatchloom.connecthub.connecthub_service.enums.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponse(Integer id,
                                   Integer recipientId,
                                   Integer senderUserId,
                                   NotificationType type,
                                   String message,
                                   Integer classifiedPostId,
                                   Integer conversationId,
                                   Integer messageId,
                                   boolean isRead,
                                   LocalDateTime createdAt,
                                   LocalDateTime readAt) {
}
