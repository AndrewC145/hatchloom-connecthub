package com.hatchloom.connecthub.connecthub_service.factory;

import com.hatchloom.connecthub.connecthub_service.model.Post;
import com.hatchloom.connecthub.connecthub_service.model.SharePost;
import org.springframework.stereotype.Component;

@Component("shareFactory")
public class ShareFactory extends PostFactory {
    @Override
    public Post createPost(String title, String content, Integer authorId) {
        return new SharePost(title, content, authorId);
    }

}
