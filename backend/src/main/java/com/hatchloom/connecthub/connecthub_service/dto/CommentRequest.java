package com.hatchloom.connecthub.connecthub_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for handling comment creation requests
 * @param userId
 * @param postId
 * @param commentText
 */
public record CommentRequest(
        @NotNull(message = "User ID must not be null") Integer userId,
        @NotNull(message = "Post ID must not be null") Integer postId,
        @NotBlank(message = "Comment text must not be blank") String commentText
) {
}

