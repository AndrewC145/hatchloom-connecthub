package com.hatchloom.connecthub.connecthub_service.observer;

import com.hatchloom.connecthub.connecthub_service.model.Messages;

public interface MessageObserver {
    void update(Messages message, Integer receiverUserId);
}
