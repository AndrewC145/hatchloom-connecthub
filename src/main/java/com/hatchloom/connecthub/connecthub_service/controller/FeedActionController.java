package com.hatchloom.connecthub.connecthub_service.controller;

import com.hatchloom.connecthub.connecthub_service.dto.CommentRequest;
import com.hatchloom.connecthub.connecthub_service.dto.CommentResponse;
import com.hatchloom.connecthub.connecthub_service.dto.LikeRequest;
import com.hatchloom.connecthub.connecthub_service.dto.PostActionsResponse;
import com.hatchloom.connecthub.connecthub_service.model.FeedAction;
import com.hatchloom.connecthub.connecthub_service.service.FeedActionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feed/actions")
public class FeedActionController {
    private final FeedActionService feedActionService;

    public FeedActionController(FeedActionService feedActionService) {
        this.feedActionService = feedActionService;
    }

    @PostMapping("/like")
    public ResponseEntity<String> likePost(@Valid @RequestBody LikeRequest request) {
        try {
            feedActionService.likePost(request);
            return new ResponseEntity<>("Post liked successfully", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/like")
    public ResponseEntity<String> unlikePost(
            @RequestParam Integer postId,
            @RequestParam Integer userId) {
        try {
            feedActionService.unlikePost(postId, userId);
            return new ResponseEntity<>("Post unliked successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/comment")
    public ResponseEntity<String> addComment(@Valid @RequestBody CommentRequest request) {
        try {
            feedActionService.addComment(request);
            return new ResponseEntity<>("Comment added successfully", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Integer commentId,
            @RequestParam Integer userId) {
        try {
            feedActionService.deleteComment(commentId, userId);
            return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getPostActions(
            @PathVariable Integer postId,
            @RequestParam(required = false) Integer userId) {
        try {
            PostActionsResponse actions = feedActionService.getPostActions(postId, userId);
            return new ResponseEntity<>(actions, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/post/{postId}/comments")
    public ResponseEntity<?> getComments(@PathVariable Integer postId) {
        try {
            List<CommentResponse> comments = feedActionService.getCommentsByPostId(postId);
            return new ResponseEntity<>(comments, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/post/{postId}/likes/count")
    public ResponseEntity<?> getLikesCount(@PathVariable Integer postId) {
        try {
            Long count = feedActionService.getLikesCount(postId);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/comment/{commentId}/like")
    public ResponseEntity<?> likeComment(
            @PathVariable Integer commentId,
            @RequestParam Integer userId) {
        try {
            FeedAction like = feedActionService.likeComment(commentId, userId);
            return new ResponseEntity<>(like, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/comment/{commentId}/like")
    public ResponseEntity<String> unlikeComment(
            @PathVariable Integer commentId,
            @RequestParam Integer userId) {
        try {
            feedActionService.unlikeComment(commentId, userId);
            return new ResponseEntity<>("Comment unliked successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/comment/{commentId}/likes/count")
    public ResponseEntity<?> getCommentLikesCount(@PathVariable Integer commentId) {
        try {
            Long count = feedActionService.getCommentLikesCount(commentId);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}


