package com.hatchloom.connecthub.connecthub_service.factory;

import com.hatchloom.connecthub.connecthub_service.model.AnnouncementPost;
import com.hatchloom.connecthub.connecthub_service.model.Post;
import org.springframework.stereotype.Component;

@Component("announcementFactory")
public class AnnouncementFactory extends PostFactory {
    @Override
    public Post createPost(String title, String content, Integer authorId) {
        return new AnnouncementPost(title, content, authorId);
    }
}
