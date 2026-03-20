package com.hatchloom.connecthub.connecthub_service.dto;

public record SendMessageRequest(Integer conversationId, Integer senderId, String content) {

}
