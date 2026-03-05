package com.hatchloom.connecthub.connecthub_service.factory;

import com.hatchloom.connecthub.connecthub_service.model.Post;

public abstract class PostFactory {
    abstract Post createPost(String title, String content, Integer authorId);
}

