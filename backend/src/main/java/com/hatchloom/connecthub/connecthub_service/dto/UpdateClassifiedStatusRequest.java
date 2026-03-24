package com.hatchloom.connecthub.connecthub_service.dto;

import java.util.UUID;

/**
 * DTO for handling update classified status requests
 * @param userId
 * @param newStatus
 */
public record UpdateClassifiedStatusRequest(UUID userId, String newStatus) {
}
