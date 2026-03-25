package com.hatchloom.connecthub.connecthub_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO for handling comment creation requests
 * @param postId
 * @param commentText
 */
public record CommentRequest(
        @NotNull(message = "Post ID must not be null") Integer postId,
        @NotBlank(message = "Comment text must not be blank") String commentText
) {
}

