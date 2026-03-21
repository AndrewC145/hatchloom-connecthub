package com.hatchloom.connecthub.connecthub_service.repository;

import com.hatchloom.connecthub.connecthub_service.enums.NotificationType;
import com.hatchloom.connecthub.connecthub_service.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing Notification entities
 */
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByRecipientUserIdAndTypeOrderByCreatedAtDesc(Integer recipientUserId, NotificationType type);
    List<Notification> findByRecipientUserIdAndTypeAndIsReadFalseOrderByCreatedAtDesc(Integer recipientUserId, NotificationType type);
}
