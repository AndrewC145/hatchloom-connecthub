package com.hatchloom.connecthub.connecthub_service.observer;

import com.hatchloom.connecthub.connecthub_service.builder.NotificationBuilder;
import com.hatchloom.connecthub.connecthub_service.enums.NotificationType;
import com.hatchloom.connecthub.connecthub_service.model.ClassifiedPost;
import com.hatchloom.connecthub.connecthub_service.service.NotificationService;
import org.springframework.stereotype.Component;

@Component
public class ClassifiedNotificationObserver implements ClassifiedObserver {
    private final NotificationService notificationService;

    public ClassifiedNotificationObserver(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @Override
    public void update(ClassifiedPost post, Integer receiverUserId) {
        String shortenedTitle = post.getTitle().substring(0, Math.min(20, post.getTitle().length()));
        notificationService.createNotification(
                new NotificationBuilder()
                .setRecipientUserId(receiverUserId)
                .setSenderUserId(post.getAuthor())
                .setType(NotificationType.CLASSIFIED_CREATED)
                .setMessage("New classified post: " + shortenedTitle)
                .setClassifiedPostId(post.getId()));
    }
}
