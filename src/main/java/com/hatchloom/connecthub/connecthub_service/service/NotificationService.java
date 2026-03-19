package com.hatchloom.connecthub.connecthub_service.service;

import com.hatchloom.connecthub.connecthub_service.builder.NotificationBuilder;
import com.hatchloom.connecthub.connecthub_service.model.Notification;
import com.hatchloom.connecthub.connecthub_service.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createNotification(NotificationBuilder builder) {
        Notification notification = builder.build();
        notificationRepository.save(notification);
    }

    public List<Notification> getNotifications(Integer userId, boolean unread) {
        if (unread) {
            return notificationRepository.findByRecipientUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
        }
        else {
            return notificationRepository.findByRecipientUserIdOrderByCreatedAtDesc(userId);
        }
    }

    public void markAsRead(Integer notificationId, Integer userId) {
        Optional<Notification> notification = Optional.of(notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification does not exist")));

        Notification n = notification.get();
        if (!n.getRecipientUserId().equals(userId)) {
            throw new IllegalArgumentException("Notification does not belong to this user");
        }

        n.setRead(true);
        n.setReadAt(LocalDateTime.now());
        notificationRepository.save(n);
    }


}
