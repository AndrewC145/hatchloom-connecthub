package com.hatchloom.connecthub.connecthub_service.service;

import com.hatchloom.connecthub.connecthub_service.dto.CommentRequest;
import com.hatchloom.connecthub.connecthub_service.dto.CommentResponse;
import com.hatchloom.connecthub.connecthub_service.dto.LikeRequest;
import com.hatchloom.connecthub.connecthub_service.dto.PostActionsResponse;
import com.hatchloom.connecthub.connecthub_service.enums.ActionType;
import com.hatchloom.connecthub.connecthub_service.model.FeedAction;
import com.hatchloom.connecthub.connecthub_service.model.Post;
import com.hatchloom.connecthub.connecthub_service.repository.FeedActionRepository;
import com.hatchloom.connecthub.connecthub_service.repository.FeedPostRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedActionService {
    private final FeedActionRepository feedActionRepository;
    private final FeedPostRepository feedPostRepository;

    public FeedActionService(FeedActionRepository feedActionRepository, FeedPostRepository feedPostRepository) {
        this.feedActionRepository = feedActionRepository;
        this.feedPostRepository = feedPostRepository;
    }

    @Transactional
    public void likePost(LikeRequest request) {
        Post post = feedPostRepository.getPostById(request.postId());
        if (post == null) {
            throw new IllegalArgumentException("Post with ID " + request.postId() + " not found");
        }

        Optional<FeedAction> existingLike = feedActionRepository.findByPostIdAndUserIdAndActionType(
                request.postId(), request.userId(), ActionType.LIKE.getValue());

        if (existingLike.isPresent()) {
            throw new IllegalArgumentException("User has already liked this post");
        }

        FeedAction like = new FeedAction();
        like.setPost(post);
        like.setPostId(request.postId());
        like.setUserId(request.userId());
        like.setActionType(ActionType.LIKE.getValue());

        feedActionRepository.save(like);
    }

    @Transactional
    public void unlikePost(Integer postId, Integer userId) {
        Optional<FeedAction> existingLike = feedActionRepository.findByPostIdAndUserIdAndActionType(
                postId, userId, ActionType.LIKE.getValue());

        if (existingLike.isEmpty()) {
            throw new IllegalArgumentException("User has not liked this post");
        }

        feedActionRepository.delete(existingLike.get());
    }

    @Transactional
    public void addComment(CommentRequest request) {
        Post post = feedPostRepository.getPostById(request.postId());
        if (post == null) {
            throw new IllegalArgumentException("Post with ID " + request.postId() + " not found");
        }

        if (request.commentText() == null || request.commentText().trim().isEmpty()) {
            throw new IllegalArgumentException("Comment text cannot be empty");
        }

        FeedAction comment = new FeedAction();
        comment.setPost(post);
        comment.setPostId(request.postId());
        comment.setUserId(request.userId());
        comment.setActionType(ActionType.COMMENT.getValue());
        comment.setCommentText(request.commentText().trim());

        feedActionRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Integer commentId, Integer userId) {
        Optional<FeedAction> comment = feedActionRepository.findByIdAndUserIdAndActionType(
                commentId, userId, ActionType.COMMENT.getValue());

        if (comment.isEmpty()) {
            throw new IllegalArgumentException("Comment not found or user is not authorized to delete this comment");
        }

        feedActionRepository.delete(comment.get());
    }

    @Transactional
    public FeedAction likeComment(Integer commentId, Integer userId) {
        FeedAction comment = feedActionRepository.getFeedActionById(commentId);
        if (comment == null || !comment.getActionType().equals(ActionType.COMMENT.getValue())) {
            throw new IllegalArgumentException("Comment with ID " + commentId + " not found");
        }

        Optional<FeedAction> existingLike = feedActionRepository.findByParentActionIdAndUserIdAndActionType(
                commentId, userId, ActionType.LIKE.getValue());

        if (existingLike.isPresent()) {
            throw new IllegalArgumentException("User has already liked this comment");
        }
        FeedAction commentLike = new FeedAction();
        commentLike.setPost(comment.getPost());
        commentLike.setPostId(comment.getPostId());
        commentLike.setUserId(userId);
        commentLike.setActionType(ActionType.LIKE.getValue());
        commentLike.setParentActionId(commentId);

        return feedActionRepository.save(commentLike);
    }

    @Transactional
    public void unlikeComment(Integer commentId, Integer userId) {
        Optional<FeedAction> existingLike = feedActionRepository.findByParentActionIdAndUserIdAndActionType(
                commentId, userId, ActionType.LIKE.getValue());

        if (existingLike.isEmpty()) {
            throw new IllegalArgumentException("User has not liked this comment");
        }

        feedActionRepository.delete(existingLike.get());
    }

    public Long getCommentLikesCount(Integer commentId) {
        return feedActionRepository.countLikesByCommentId(commentId);
    }

    public PostActionsResponse getPostActions(Integer postId, Integer currentUserId) {
        Post post = feedPostRepository.getPostById(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post with ID " + postId + " not found");
        }


        Long likesCount = feedActionRepository.countLikesByPostId(postId);
        Long commentsCount = feedActionRepository.countCommentsByPostId(postId);

        List<FeedAction> commentActions = feedActionRepository.findCommentsByPostId(postId);
        List<CommentResponse> comments = commentActions.stream()
                .map(action -> new CommentResponse(
                        action.getId(),
                        action.getPostId(),
                        action.getUserId(),
                        action.getCommentText(),
                        action.getCreatedAt()
                ))
                .collect(Collectors.toList());

        boolean isLikedByCurrentUser = currentUserId != null &&
                feedActionRepository.findByPostIdAndUserIdAndActionType(
                        postId, currentUserId, ActionType.LIKE.getValue()).isPresent();

        return new PostActionsResponse(postId, likesCount, commentsCount, comments, isLikedByCurrentUser);
    }

    public List<CommentResponse> getCommentsByPostId(Integer postId) {
        List<FeedAction> commentActions = feedActionRepository.findCommentsByPostId(postId);
        return commentActions.stream()
                .map(action -> new CommentResponse(
                        action.getId(),
                        action.getPostId(),
                        action.getUserId(),
                        action.getCommentText(),
                        action.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public Long getLikesCount(Integer postId) {
        return feedActionRepository.countLikesByPostId(postId);
    }
}
