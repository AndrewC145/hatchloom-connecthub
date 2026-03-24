package com.hatchloom.connecthub.connecthub_service.dto;

import java.time.LocalDateTime;

/**
 * DTO for handling send message responses
 * @param conversationId
 * @param messageId
 * @param senderId
 * @param recipientId
 * @param content
 * @param createdAt
 */
public record SendMessageResponse(
Integer conversationId,
  Integer messageId,
  Integer senderId,
  Integer recipientId,
  String content,
  LocalDateTime createdAt) {
}
