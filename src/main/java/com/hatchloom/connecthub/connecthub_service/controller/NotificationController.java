package com.hatchloom.connecthub.connecthub_service.controller;

import com.hatchloom.connecthub.connecthub_service.model.Notification;
import com.hatchloom.connecthub.connecthub_service.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{userId}/classified")
    public ResponseEntity<List<Notification>> getClassifiedNotifications(@PathVariable Integer userId, @RequestParam boolean unread) {
        return ResponseEntity.ok(notificationService.getClassifiedNotifications(userId, unread));
    }

    @GetMapping("/{userId}/messages")
    public ResponseEntity<List<Notification>> getMessageNotifications(@PathVariable Integer userId, @RequestParam boolean unread) {
        return ResponseEntity.ok(notificationService.getMessageNotifications(userId, unread));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Integer notificationId,
                                           @RequestBody Integer userId) {
        notificationService.markAsRead(notificationId, userId);
        return ResponseEntity.ok().build();
    }
}
