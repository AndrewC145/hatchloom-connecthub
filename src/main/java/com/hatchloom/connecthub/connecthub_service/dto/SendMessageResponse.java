package com.hatchloom.connecthub.connecthub_service.dto;

import java.time.LocalDateTime;

public record SendMessageResponse(
Integer conversationId,
  Integer messageId,
  Integer senderId,
  Integer recipientId,
  String content,
  LocalDateTime createdAt) {
}
