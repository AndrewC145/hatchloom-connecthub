package com.hatchloom.connecthub.connecthub_service.service;

import com.hatchloom.connecthub.connecthub_service.dto.ClassifiedPostCreationRequest;
import com.hatchloom.connecthub.connecthub_service.model.ClassifiedPost;
import com.hatchloom.connecthub.connecthub_service.repository.ClassifiedPostRepository;
import org.springframework.stereotype.Service;
import java.util.List;

// Service class for managing classified posts, including creation, retrieval, filtering, and status updates.
// Base implementation for now, pagination and user authorization will be added later
@Service
public class ClassifiedPostService {
    private final ClassifiedPostRepository classifiedPostRepository;

    public ClassifiedPostService(ClassifiedPostRepository classifiedPostRepository) {
        this.classifiedPostRepository = classifiedPostRepository;
    }


    public ClassifiedPost createClassifiedPost(ClassifiedPostCreationRequest request) {
        if (validateClassifiedRequest(request)) {
            throw new IllegalArgumentException("Invalid classified post creation request");
        }

        ClassifiedPost post = new ClassifiedPost();
        post.setTitle(request.basePost().title());
        post.setContent(request.basePost().content());
        post.setAuthor(request.basePost().authorId());
        post.setProjectId(request.projectId());
        post.setStatus(request.status());

        return classifiedPostRepository.save(post);
    }

    public ClassifiedPost getClassifiedById(Integer postId) {
        if (postId == null) {
            throw new IllegalArgumentException("Post ID must not be null");
        }
        if (postId <= 0) {
            throw new IllegalArgumentException("Post ID must be a positive integer");
        }

        if (!classifiedPostRepository.existsById(postId)) {
            throw new IllegalArgumentException("Post with ID " + postId + " does not exist");
        }
        return classifiedPostRepository.getClassifiedPostById(postId).orElseThrow(() -> new IllegalArgumentException("Post with ID " + postId + " does not exist"));
    }

    public List<ClassifiedPost> filterClassifiedPostsByStatus(String status) {
        if (validateStatus(status)) {
            throw new IllegalArgumentException("Status must be 'open', 'filled', or 'closed'");
        }

        String normalizedStatus = status.trim().toLowerCase();
        return classifiedPostRepository.findByStatus(normalizedStatus);
    }

    private boolean validateStatus(String status) {
        return status == null || (!status.equals("open") && !status.equals("filled") && !status.equals("closed"));
    }

    public ClassifiedPost updateClassifiedPostStatus(Integer postId, Integer userId, String newStatus) {
        if (postId == null) {
            throw new IllegalArgumentException("Post ID must not be null");
        }

        if (validateStatus(newStatus)) {
            throw new IllegalArgumentException("Status must be 'open', 'filled', or 'closed'");
        }

        ClassifiedPost post = classifiedPostRepository.getClassifiedPostById(postId).orElseThrow(() -> new IllegalArgumentException("Post with ID " + postId + " does not exist"));

        if (!post.getAuthor().equals(userId)) {
            throw new IllegalArgumentException("Only the author can update the post status");
        }

        post.setStatus(newStatus);
        return classifiedPostRepository.save(post);
    }

    // Template method for now, pagination of some sort will be better performance wise
    public List<ClassifiedPost> getAllClassifiedPosts() {
        return classifiedPostRepository.findAll();
    }

    private boolean validateClassifiedRequest(ClassifiedPostCreationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request must not be null");
        }

        if (request.basePost().title() == null || request.basePost().title().trim().isEmpty()) {
            throw new IllegalArgumentException("Title must not be null or empty");
        }
        if (request.basePost().content() == null || request.basePost().content().trim().isEmpty()) {
            throw new IllegalArgumentException("Content must not be null or empty");
        }

        if (request.basePost().authorId() == null) {
            throw new IllegalArgumentException("Author ID must not be null");
        }

        if (request.projectId() == null) {
            throw new IllegalArgumentException("Project ID must not be null");
        }

        if (request.basePost().title().length() > 255) {
            throw new IllegalArgumentException("Title must not exceed 255 characters");
        }
        if (request.basePost().content().length() > 3000) {
            throw new IllegalArgumentException("Content must not exceed 3000 characters");
        }

        if (validateStatus(request.status())) {
            throw new IllegalArgumentException("Status must be 'open', 'filled', or 'closed'");
        }

        if (request.projectId() <= 0) {
            throw new IllegalArgumentException("Project ID must be a positive integer");
        }

        return false;
    }
}
