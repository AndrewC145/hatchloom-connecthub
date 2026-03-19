package com.hatchloom.connecthub.connecthub_service.repository;

import com.hatchloom.connecthub.connecthub_service.enums.NotificationType;
import com.hatchloom.connecthub.connecthub_service.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByRecipientUserIdOrderByCreatedAtDesc(Integer recipientUserId);
    List<Notification> findByRecipientUserIdAndIsReadFalseOrderByCreatedAtDesc(Integer recipientUserId);

    List<Notification> findByRecipientUserIdAndTypeOrderByCreatedAtDesc(Integer recipientUserId, NotificationType type);
    List<Notification> findByRecipientUserIdAndTypeAndIsReadFalseOrderByCreatedAtDesc(Integer recipientUserId, NotificationType type);
}
