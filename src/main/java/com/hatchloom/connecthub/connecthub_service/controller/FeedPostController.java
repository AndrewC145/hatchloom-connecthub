package com.hatchloom.connecthub.connecthub_service.controller;

import com.hatchloom.connecthub.connecthub_service.dto.PostCreationRequest;
import com.hatchloom.connecthub.connecthub_service.model.Post;
import com.hatchloom.connecthub.connecthub_service.service.FeedPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            return ResponseEntity.ok(createdPost);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping()
    public ResponseEntity<List<Post>> getFeedPosts() {
        List<Post> posts = feedPostService.getAllFeedPosts();
        return ResponseEntity.ok(posts);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(
            @PathVariable Integer postId,
            @RequestParam Integer userId) {
        try {
            feedPostService.deleteFeedPost(postId, userId);
            return ResponseEntity.ok("Post deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
