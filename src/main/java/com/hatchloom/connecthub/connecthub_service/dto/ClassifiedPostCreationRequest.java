package com.hatchloom.connecthub.connecthub_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ClassifiedPostCreationRequest(
        @Valid @NotNull BasePostRequest basePost,
        @NotNull(message = "Project ID must not be null") Integer projectId,
        @Pattern(regexp = "^(open|filled|closed)$", message = "Status must be 'open', 'filled', or 'closed'")
        String status
) {
}



