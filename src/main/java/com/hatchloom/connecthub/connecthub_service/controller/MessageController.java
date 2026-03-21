package com.hatchloom.connecthub.connecthub_service.controller;

import com.hatchloom.connecthub.connecthub_service.dto.MessageResponse;
import com.hatchloom.connecthub.connecthub_service.dto.SendMessageRequest;
import com.hatchloom.connecthub.connecthub_service.dto.SendMessageResponse;
import com.hatchloom.connecthub.connecthub_service.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing messages, including sending messages and retrieving conversation messages.
 */
@RestController
@RequestMapping("/api/message")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/{recipientId}/send")
    public ResponseEntity<SendMessageResponse> sendMessage(@PathVariable Integer recipientId, @RequestBody SendMessageRequest request) {
        try {
            SendMessageResponse response = messageService.sendMessage(request.conversationId(), request.senderId(), recipientId, request.content());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<MessageResponse>> getConversationMessages(@PathVariable Integer conversationId, @RequestParam Integer userId) {
        try {
            List<MessageResponse> msgs = messageService.getConversationMessages(conversationId, userId);
            return new ResponseEntity<>(msgs, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
