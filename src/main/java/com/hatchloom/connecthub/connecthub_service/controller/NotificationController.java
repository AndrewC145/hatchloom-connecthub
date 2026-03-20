package com.hatchloom.connecthub.connecthub_service.controller;

import com.hatchloom.connecthub.connecthub_service.dto.NotificationResponse;
import com.hatchloom.connecthub.connecthub_service.model.Notification;
import com.hatchloom.connecthub.connecthub_service.service.NotificationService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<NotificationResponse>> getClassifiedNotifications(@PathVariable Integer userId, @RequestParam boolean unread) {
        return new ResponseEntity<>(notificationService.getClassifiedNotifications(userId, unread), HttpStatus.OK);
    }

    @GetMapping("/{userId}/messages")
    public ResponseEntity<List<NotificationResponse>> getMessageNotifications(@PathVariable Integer userId, @RequestParam boolean unread) {
        return new ResponseEntity<>(notificationService.getMessageNotifications(userId, unread), HttpStatus.OK);
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Integer notificationId,
                                           @RequestBody Integer userId) {
        notificationService.markAsRead(notificationId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
