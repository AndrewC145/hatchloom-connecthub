package com.hatchloom.connecthub.connecthub_service.controller;

import com.hatchloom.connecthub.connecthub_service.dto.CursorResponse;
import com.hatchloom.connecthub.connecthub_service.dto.FeedPostResponse;
import com.hatchloom.connecthub.connecthub_service.dto.PostCreationRequest;
import com.hatchloom.connecthub.connecthub_service.model.Post;
import com.hatchloom.connecthub.connecthub_service.service.FeedPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing feed posts, and
 * retrieving posts with pagination
 */
@RestController
@RequestMapping("/api/feed")
public class FeedPostController {
    private final FeedPostService feedPostService;

    public FeedPostController(FeedPostService feedPostService) {
        this.feedPostService = feedPostService;
    }

    @PostMapping()
    public ResponseEntity<Post> createPost(@RequestBody PostCreationRequest request) {
        try {
            Post createdPost = feedPostService.createFeedPost(request);
            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping()
    public ResponseEntity<CursorResponse<FeedPostResponse>> getFeedPosts(@RequestParam(defaultValue = "25") Integer limit,
                                                                         @RequestParam(required = false) String after) {
        try {
            CursorResponse<Post> page = feedPostService.getAllFeedPosts(after, limit);

            List<FeedPostResponse> feedPostResponses = page.getData().stream()
                    .map(FeedPostResponse::from)
                    .toList();

            CursorResponse<FeedPostResponse> response = new CursorResponse<>(feedPostResponses, page.getNextCursor(), page.isHasNext());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(
            @PathVariable Integer postId,
            @RequestParam UUID userId) {
        try {
            feedPostService.deleteFeedPost(postId, userId);
            return new ResponseEntity<>("Post deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
