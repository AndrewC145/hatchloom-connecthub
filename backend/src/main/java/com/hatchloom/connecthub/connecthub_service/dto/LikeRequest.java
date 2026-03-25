package com.hatchloom.connecthub.connecthub_service.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO for handling like post requests
 * @param postId
 */
public record LikeRequest(
        @NotNull(message = "Post ID must not be null") Integer postId
) {
}

