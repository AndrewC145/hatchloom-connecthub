package com.hatchloom.connecthub.connecthub_service.observer;

import com.hatchloom.connecthub.connecthub_service.model.Post;

public interface FeedSubject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(Post post);
}
