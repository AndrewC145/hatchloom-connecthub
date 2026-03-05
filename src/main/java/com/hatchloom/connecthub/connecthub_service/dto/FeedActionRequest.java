package com.hatchloom.connecthub.connecthub_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record FeedActionRequest(
        @NotNull(message = "User ID must not be null") Integer userId,
        @NotNull(message = "Post ID must not be null") Integer postId,
        @NotNull(message = "Action must not be null")
        @Pattern(regexp = "^(like|comment)$", message = "Action must be either 'like' or 'comment'")
        String action,
        String commentText
) {

}
