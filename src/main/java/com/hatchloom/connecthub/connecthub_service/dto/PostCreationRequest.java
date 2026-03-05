package com.hatchloom.connecthub.connecthub_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PostCreationRequest(
        @Valid @NotNull BasePostRequest basePost,
        @NotNull(message = "Post type must not be null")
        @Pattern(regexp = "^(share|announcement|achievement)$", message = "Post type must be 'share', 'announcement', or 'achievement'")
        String postType
) {
}