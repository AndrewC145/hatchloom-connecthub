package com.hatchloom.connecthub.connecthub_service.controller;

import com.hatchloom.connecthub.connecthub_service.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/{recipientId}/send")
    public ResponseEntity<Void> sendMessage(Integer conversationId, Integer senderId, @PathVariable Integer recipientId, String content) {
        try {
            messageService.sendMessage(conversationId, senderId, recipientId, content);
            return ResponseEntity.ok().build();
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
