package com.hatchloom.connecthub.connecthub_service.controller;

import com.hatchloom.connecthub.connecthub_service.dto.NotificationResponse;
import com.hatchloom.connecthub.connecthub_service.dto.NotificationSummaryResponse;
import com.hatchloom.connecthub.connecthub_service.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for retrieving notifications for messages and classified posts,
 * and marking notifications as read
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{userId}/all")
    public ResponseEntity<?> getNotificationSummary(@PathVariable UUID userId, @RequestParam(defaultValue = "true") boolean unread,
                                                    @RequestParam(defaultValue = "5") int limit) {
        try {
            NotificationSummaryResponse summary = notificationService.getNotificationSummary(userId, unread, limit);
            return new ResponseEntity<>(summary, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/classified")
    public ResponseEntity<List<NotificationResponse>> getClassifiedNotifications(@PathVariable UUID userId, @RequestParam boolean unread) {
        return new ResponseEntity<>(notificationService.getClassifiedNotifications(userId, unread), HttpStatus.OK);
    }

    @GetMapping("/{userId}/messages")
    public ResponseEntity<List<NotificationResponse>> getMessageNotifications(@PathVariable UUID userId, @RequestParam boolean unread) {
        return new ResponseEntity<>(notificationService.getMessageNotifications(userId, unread), HttpStatus.OK);
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(@PathVariable Integer notificationId,
                                           @RequestBody UUID userId) {
        try {
            notificationService.markAsRead(notificationId, userId);
            return new ResponseEntity<>("Notification marked as read", HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
