package com.hatchloom.connecthub.connecthub_service.observer;

import com.hatchloom.connecthub.connecthub_service.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserFeed implements Observer {
    private final Integer userId;
    private final List<Post> posts = new CopyOnWriteArrayList<>();

    public UserFeed(Integer userId) {
        this.userId = userId;
    }

    @Override
    public void update(Post post) {
        posts.addFirst(post);
    }

    public List<Post> getPosts() {
        return new ArrayList<>(posts);
    }

    public int getUserId() {
        return userId;
    }
}
