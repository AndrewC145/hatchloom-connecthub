package com.hatchloom.connecthub.connecthub_service.dto;

import java.time.LocalDateTime;

public record MessageResponse(
        Integer id,
        Integer conversationId,
        Integer senderId,
        String content,
        LocalDateTime createdAt) {

}