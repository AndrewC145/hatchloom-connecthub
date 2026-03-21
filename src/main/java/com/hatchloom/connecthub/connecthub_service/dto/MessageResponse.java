package com.hatchloom.connecthub.connecthub_service.dto;

import java.time.LocalDateTime;

/**
 * DTO for handling message responses
 * @param id
 * @param conversationId
 * @param senderId
 * @param content
 * @param createdAt
 */
public record MessageResponse(
        Integer id,
        Integer conversationId,
        Integer senderId,
        String content,
        LocalDateTime createdAt) {

}