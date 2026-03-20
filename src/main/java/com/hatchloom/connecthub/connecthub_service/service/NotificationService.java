package com.hatchloom.connecthub.connecthub_service.service;

import com.hatchloom.connecthub.connecthub_service.builder.NotificationBuilder;
import com.hatchloom.connecthub.connecthub_service.dto.NotificationResponse;
import com.hatchloom.connecthub.connecthub_service.enums.NotificationType;
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

    public List<NotificationResponse> getClassifiedNotifications(Integer userId, boolean unread) {
        List<Notification> notifications;
        if (unread) {
            notifications = notificationRepository.findByRecipientUserIdAndTypeAndIsReadFalseOrderByCreatedAtDesc(
                    userId, NotificationType.CLASSIFIED_CREATED
            );
        }
        else {
            notifications = notificationRepository.findByRecipientUserIdAndTypeOrderByCreatedAtDesc(
                    userId, NotificationType.CLASSIFIED_CREATED
            );
        }

        return notifications.stream().map(m -> new NotificationResponse(
                m.getId(),
                m.getRecipientUserId(),
                m.getSenderUserId(),
                m.getType(),
                m.getMessage(),
                m.getClassifiedPostId(),
                m.getConversationId(),
                m.getMessageId(),
                m.isRead(),
                m.getCreatedAt(),
                m.getReadAt()
        )).toList();
    }

    public List<NotificationResponse> getMessageNotifications(Integer userId, boolean unread) {
        List<Notification> msgNotifications;
        if (unread) {
            msgNotifications = notificationRepository.findByRecipientUserIdAndTypeAndIsReadFalseOrderByCreatedAtDesc(
                    userId, NotificationType.MESSAGE
            );
        }
        else {
            msgNotifications = notificationRepository.findByRecipientUserIdAndTypeOrderByCreatedAtDesc(
                    userId, NotificationType.MESSAGE
            );
        }

        return msgNotifications.stream().map(m -> new NotificationResponse(
                m.getId(),
                m.getRecipientUserId(),
                m.getSenderUserId(),
                m.getType(),
                m.getMessage(),
                m.getClassifiedPostId(),
                m.getConversationId(),
                m.getMessageId(),
                m.isRead(),
                m.getCreatedAt(),
                m.getReadAt()
        )).toList();
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
