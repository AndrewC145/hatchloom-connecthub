package com.hatchloom.connecthub.connecthub_service.dto;

import jakarta.validation.constraints.NotNull;

public record LikeRequest(
        @NotNull(message = "User ID must not be null") Integer userId,
        @NotNull(message = "Post ID must not be null") Integer postId
) {
}

