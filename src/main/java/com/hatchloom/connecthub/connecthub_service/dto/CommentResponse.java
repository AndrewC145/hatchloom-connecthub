package com.hatchloom.connecthub.connecthub_service.dto;

import java.time.LocalDateTime;

public record CommentResponse(
        Integer id,
        Integer postId,
        Integer userId,
        String commentText,
        LocalDateTime createdAt
) {
}

