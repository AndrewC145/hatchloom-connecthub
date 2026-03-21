package com.hatchloom.connecthub.connecthub_service.dto;

/**
 * DTO for handling send message requests
 * @param conversationId
 * @param senderId
 * @param content
 */
public record SendMessageRequest(Integer conversationId, Integer senderId, String content) {

}
