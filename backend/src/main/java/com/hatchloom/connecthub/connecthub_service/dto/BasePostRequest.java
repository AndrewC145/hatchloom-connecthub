package com.hatchloom.connecthub.connecthub_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Base DTO for handling common fields for post types
 * @param title
 * @param content
 * @param authorId
 */
public record BasePostRequest(
        @NotBlank(message = "Title must not be blank") String title,
        @NotBlank(message = "Content must not be blank") String content,
        @NotNull(message = "Author ID must not be null") UUID authorId
) {
}

