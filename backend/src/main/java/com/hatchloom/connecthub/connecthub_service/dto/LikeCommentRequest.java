package com.hatchloom.connecthub.connecthub_service.dto;

import java.util.UUID;

/**
 * DTO for handling like comment requests
 * @param userId
 */
public record LikeCommentRequest(UUID userId) {
}
