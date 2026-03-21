package com.hatchloom.connecthub.connecthub_service.service;

import com.hatchloom.connecthub.connecthub_service.dto.ClassifiedPostCreationRequest;
import com.hatchloom.connecthub.connecthub_service.dto.CursorResponse;
import com.hatchloom.connecthub.connecthub_service.model.ClassifiedPost;
import com.hatchloom.connecthub.connecthub_service.observer.ClassifiedPostFeed;
import com.hatchloom.connecthub.connecthub_service.repository.ClassifiedPostRepository;
import com.hatchloom.connecthub.connecthub_service.utils.ClassifiedCursorPayload;
import com.hatchloom.connecthub.connecthub_service.utils.CursorPaginationCodec;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for managing classified posts, including creation,
 * retrieval, status updates, and cursor-based pagination
 */
@Service
public class ClassifiedPostService {
    private final ClassifiedPostRepository classifiedPostRepository;
    private final ClassifiedPostFeed classifiedPostFeed;
    private final CursorPaginationService cursorPaginationService;

    public ClassifiedPostService(ClassifiedPostRepository classifiedPostRepository, ClassifiedPostFeed classifiedPostFeed, CursorPaginationService cursorPaginationService) {
        this.classifiedPostRepository = classifiedPostRepository;
        this.classifiedPostFeed = classifiedPostFeed;
        this.cursorPaginationService = cursorPaginationService;
    }


    /**
     * Creates a new classified post based on the provided request
     * @param request the incoming request containing the details
     * @return the created ClassifiedPost entity
     */
    public ClassifiedPost createClassifiedPost(ClassifiedPostCreationRequest request) {
        if (validateClassifiedRequest(request)) {
            throw new IllegalArgumentException("Invalid classified post creation request");
        }

        Integer authorId = request.basePost().authorId();

        ClassifiedPost post = new ClassifiedPost();
        post.setTitle(request.basePost().title());
        post.setContent(request.basePost().content());
        post.setAuthor(authorId);
        post.setProjectId(request.projectId());
        post.setStatus(request.status());

        ClassifiedPost savedPost = classifiedPostRepository.save(post);
        classifiedPostFeed.notifyObservers(savedPost, authorId);
        return savedPost;
    }

    /**
     * Retrieves a classified post by its ID
     * @param postId the ID of the classified post to retrieve
     * @return the ClassifiedPost entity with the specified ID
     */
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

    /**
     * Filters classified posts based on their status (open, filled, closed)
     * @param status the status to filter by
     * @return a list of classified posts with the status
     */
    public List<ClassifiedPost> filterClassifiedPostsByStatus(String status) {
        if (validateStatus(status)) {
            throw new IllegalArgumentException("Status must be 'open', 'filled', or 'closed'");
        }

        String normalizedStatus = status.trim().toLowerCase();
        return classifiedPostRepository.findByStatus(normalizedStatus);
    }

    /**
     * Validates the status input to ensure it is either "open", "filled", or "closed"
     * @param status the status string to validate
     * @return a boolean whether the status is valid or not
     */
    private boolean validateStatus(String status) {
        return status == null || (!status.equals("open") && !status.equals("filled") && !status.equals("closed"));
    }

    /**
     * Updates the status of a classified post
     * @param postId the post ID of the classified post to update
     * @param userId The user ID of the author
     * @param newStatus the new status to set
     * @return the updated ClassifiedPost entity
     */
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


    /**
     * Retrieves all classified posts with cursor-based pagination, filtered by status
     * @param after the cursor indicating the next start point for pagination
     * @param limit the max number of posts to return
     * @param status the status to filter by
     * @return a CursorResponse containing the paginated list of classified posts
     */
    public CursorResponse<ClassifiedPost> getAllClassifiedPosts(String after, Integer limit, String status) {
        if (validateStatus(status)) {
            throw new IllegalArgumentException("Status must be 'open', 'filled', or 'closed'");
        }

        return cursorPaginationService.paginate(after, limit, pageable -> classifiedPostRepository.findByStatusOrderByCreatedAtDescIdDesc(status, pageable),
                (payload, pageable) -> {
                    LocalDateTime createdAt = LocalDateTime.parse(payload.createdAt());
                    return classifiedPostRepository.findByStatusWithCursor(status, createdAt, payload.id(), pageable);
                },
                    cursor -> CursorPaginationCodec.decodeCursor(cursor, ClassifiedCursorPayload::new),
                payload -> CursorPaginationCodec.encodeCursor(payload.id(), payload.createdAt()),
                post -> new ClassifiedCursorPayload(post.getCreatedAt().toString(), post.getId())
                );
    }

    /**
     * Validates the classified post creation request
     * @param request the incoming request
     * @return a boolean indicating whether or not the request is valid
     */
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
