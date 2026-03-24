package com.hatchloom.connecthub.connecthub_service.observer;

import com.hatchloom.connecthub.connecthub_service.model.ClassifiedPost;

/**
 * Subject interface for managing classified post subscriptions and notifications
 */
public interface ClassifiedSubject {
    void subscribe(Integer userId);
    void unsubscribe(Integer userId);
    void notifyObservers(ClassifiedPost post, Integer receiverUserId);
}
