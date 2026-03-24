package com.hatchloom.connecthub.connecthub_service.factory;

import com.hatchloom.connecthub.connecthub_service.model.AchievementPost;
import com.hatchloom.connecthub.connecthub_service.model.Post;
import org.springframework.stereotype.Component;

/**
 * Factory for creating AchievementPost objects
 */
@Component("achievementFactory")
public class AchievementFactory extends PostFactory {
    @Override
    public Post createPost(String title, String content, Integer authorId) {
        return new AchievementPost(title, content, authorId);
    }
}
