package com.hatchloom.connecthub.connecthub_service.service;

import com.hatchloom.connecthub.connecthub_service.dto.PostCreationRequest;
import com.hatchloom.connecthub.connecthub_service.model.AchievementPost;
import com.hatchloom.connecthub.connecthub_service.model.AnnouncementPost;
import com.hatchloom.connecthub.connecthub_service.model.Post;
import com.hatchloom.connecthub.connecthub_service.model.SharePost;
import com.hatchloom.connecthub.connecthub_service.observer.PostFeed;
import com.hatchloom.connecthub.connecthub_service.repository.FeedPostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Service for managing feed post CRUD
// Base implementation for now, authorization and pagination will be added later
@Service
public class FeedPostService {
    private final FeedPostRepository feedPostRepository;
    private final PostFeed postFeed;

    public FeedPostService(FeedPostRepository feedPostRepository, PostFeed postFeed) {
        this.feedPostRepository = feedPostRepository;
        this.postFeed = postFeed;
    }

    public Post createFeedPost(PostCreationRequest request) {
        Post post;
        String title = request.basePost().title();
        String content = request.basePost().content();
        Integer authorId = request.basePost().authorId();

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Post title must not be null or blank");
        }

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Post content must not be null or blank");
        }

        if (authorId == null) {
            throw new IllegalArgumentException("Author ID must not be null");
        }

        if (request.postType() == null || request.postType().isBlank()) {
            throw new IllegalArgumentException("Post type must not be null or blank");
        }

        switch (request.postType()) {
            case "share" -> post = new SharePost(title, content, authorId);
            case "announcement" -> post = new AnnouncementPost(title, content, authorId);
            case "achievement" -> post = new AchievementPost(title, content, authorId);
            default -> throw new IllegalArgumentException("Invalid post type: " + request.postType());
        }
        Post newPost = feedPostRepository.save(post);
        postFeed.notifyObservers(newPost);
        return newPost;

    }

    public void deleteFeedPost(Integer postId, Integer userId) {
        if (postId == null) {
            throw new IllegalArgumentException("Post ID must not be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        Post post = feedPostRepository.getPostById(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post with ID " + postId + " does not exist");
        }

        if (!post.getAuthor().equals(userId)) {
            throw new IllegalArgumentException("User " + userId + " is not authorized to delete post " + postId);
        }

        feedPostRepository.deletePostById(postId);
    }

    // Template method for now, pagination of some sort will be better performance wise
    public List<Post> getAllFeedPosts() {
        return feedPostRepository.findAll();
    }
}
