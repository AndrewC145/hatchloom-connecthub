package com.hatchloom.connecthub.connecthub_service.service;

import com.hatchloom.connecthub.connecthub_service.dto.PostCreationRequest;
import com.hatchloom.connecthub.connecthub_service.model.AchievementPost;
import com.hatchloom.connecthub.connecthub_service.model.AnnouncementPost;
import com.hatchloom.connecthub.connecthub_service.model.Post;
import com.hatchloom.connecthub.connecthub_service.model.SharePost;
import com.hatchloom.connecthub.connecthub_service.repository.FeedPostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedPostService {
    private final FeedPostRepository feedPostRepository;

    public FeedPostService(FeedPostRepository feedPostRepository) {
        this.feedPostRepository = feedPostRepository;
    }


    public Post createFeedPost(PostCreationRequest request) {
        Post post;
        String title = request.basePost().title();
        String content = request.basePost().content();
        Integer authorId = request.basePost().authorId();

        switch (request.postType()) {
            case "share" -> post = new SharePost(title, content, authorId);
            case "announcement" -> post = new AnnouncementPost(title, content, authorId);
            case "achievement" -> post = new AchievementPost(title, content, authorId);
            default -> throw new IllegalArgumentException("Invalid post type: " + request.postType());
        }
        return feedPostRepository.save(post);
    }

    public void deleteFeedPost(Integer postId) {
        if (postId == null) {
            throw new IllegalArgumentException("Post ID must not be null");
        }
        Post post = feedPostRepository.getPostById(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post with ID " + postId + " does not exist");
        }

        feedPostRepository.deletePostById(postId);
    }

    // Template method for now, pagination of some sort will be better performance wise
    public List<Post> getAllFeedPosts() {
        return feedPostRepository.findAll();
    }
}
