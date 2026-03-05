package com.hatchloom.connecthub.connecthub_service.service;

import com.hatchloom.connecthub.connecthub_service.dto.ClassifiedPostCreationRequest;
import com.hatchloom.connecthub.connecthub_service.model.ClassifiedPost;
import com.hatchloom.connecthub.connecthub_service.repository.ClassifiedPostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassifiedPostService {
    private final ClassifiedPostRepository classifiedPostRepository;

    public ClassifiedPostService(ClassifiedPostRepository classifiedPostRepository) {
        this.classifiedPostRepository = classifiedPostRepository;
    }

    public ClassifiedPost createClassifiedPost(ClassifiedPostCreationRequest request) {
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
        if (!classifiedPostRepository.existsById(postId)) {
            throw new IllegalArgumentException("Post with ID " + postId + " does not exist");
        }
        return classifiedPostRepository.getClassifiedPostById(postId).orElseThrow(() -> new IllegalArgumentException("Post with ID " + postId + " does not exist"));
    }

    public List<ClassifiedPost> filterClassifiedPostsByStatus(String status) {
        if (status == null || (!status.equals("open") && !status.equals("filled") && !status.equals("closed"))) {
            throw new IllegalArgumentException("Status must be 'open', 'filled', or 'closed'");
        }
        return classifiedPostRepository.findByStatus(status);
    }

    public ClassifiedPost updateClassifiedPostStatus(Integer postId, String newStatus) {
        if (postId == null) {
            throw new IllegalArgumentException("Post ID must not be null");
        }
        if (newStatus == null || (!newStatus.equals("open") && !newStatus.equals("filled") && !newStatus.equals("closed"))) {
            throw new IllegalArgumentException("Status must be 'open', 'filled', or 'closed'");
        }

        ClassifiedPost post = classifiedPostRepository.getClassifiedPostById(postId).orElseThrow(() -> new IllegalArgumentException("Post with ID " + postId + " does not exist"));
        post.setStatus(newStatus);
        return classifiedPostRepository.save(post);
    }

    // Template method for now, pagination of some sort will be better performance wise
    public List<ClassifiedPost> getAllClassifiedPosts() {
        return classifiedPostRepository.findAll();
    }
}
