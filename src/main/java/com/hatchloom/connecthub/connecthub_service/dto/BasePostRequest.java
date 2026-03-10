package com.hatchloom.connecthub.connecthub_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BasePostRequest(
        @NotBlank(message = "Title must not be blank") String title,
        @NotBlank(message = "Content must not be blank") String content,
        @NotNull(message = "Author ID must not be null") Integer authorId
) {
}

