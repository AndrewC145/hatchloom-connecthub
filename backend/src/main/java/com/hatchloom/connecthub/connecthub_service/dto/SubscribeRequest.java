package com.hatchloom.connecthub.connecthub_service.dto;

import java.util.UUID;

/**
 * DTO for handling subscribe requests
 * @param userId
 */
public record SubscribeRequest(UUID userId) {
}
