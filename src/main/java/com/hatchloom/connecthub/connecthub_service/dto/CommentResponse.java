package com.hatchloom.connecthub.connecthub_service.dto;

import java.time.LocalDateTime;

/**
 * DTO for handling comment responses
 * @param id
 * @param postId
 * @param userId
 * @param commentText
 * @param createdAt
 */
public record CommentResponse(
        Integer id,
        Integer postId,
        Integer userId,
        String commentText,
        LocalDateTime createdAt
) {
}

