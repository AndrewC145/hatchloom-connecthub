package com.hatchloom.connecthub.connecthub_service.observer;

import com.hatchloom.connecthub.connecthub_service.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationService implements Observer {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final String userId;
    private final String feedType;

    public NotificationService(String userId, String feedType) {
        this.userId = userId;
        this.feedType = feedType;
    }
    @Override
    public void update(Post post) {
        sendNotification(post);
    }

    // Simulate sending a notification
    // Full service implementation would probably use websockets
    private void sendNotification(Post post) {
        logger.info("User {} notified for {} feed - New post: {}", userId, feedType, post.getTitle());
    }
}
