package com.hatchloom.connecthub.connecthub_service.observer;

import com.hatchloom.connecthub.connecthub_service.model.ClassifiedPost;

public interface ClassifiedObserver {
    void update(ClassifiedPost post, Integer receiverUserId);
}
