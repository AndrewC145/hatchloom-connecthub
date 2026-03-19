package com.hatchloom.connecthub.connecthub_service.controller;

import com.hatchloom.connecthub.connecthub_service.dto.ClassifiedPostCreationRequest;
import com.hatchloom.connecthub.connecthub_service.dto.CursorResponse;
import com.hatchloom.connecthub.connecthub_service.model.ClassifiedPost;
import com.hatchloom.connecthub.connecthub_service.observer.ClassifiedPostFeed;
import com.hatchloom.connecthub.connecthub_service.service.ClassifiedPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classified")
public class ClassifiedPostController {
    private final ClassifiedPostService classifiedPostService;
    private final ClassifiedPostFeed classifiedPostFeed;

    public ClassifiedPostController(ClassifiedPostService classifiedPostService, ClassifiedPostFeed classifiedPostFeed) {
        this.classifiedPostService = classifiedPostService;
        this.classifiedPostFeed = classifiedPostFeed;
    }

    @PostMapping()
    public ResponseEntity<ClassifiedPost> createClassified(@RequestBody ClassifiedPostCreationRequest request) {
        try {
            ClassifiedPost createdPost = classifiedPostService.createClassifiedPost(request);
            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping()
    public ResponseEntity<CursorResponse<ClassifiedPost>> getClassifiedPosts(@RequestParam(defaultValue = "25") Integer limit
    , @RequestParam(required = false) String after, @RequestParam(defaultValue = "open") String statusType) {
        try {
            CursorResponse<ClassifiedPost> response = classifiedPostService.getAllClassifiedPosts(after, limit, statusType);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/filtered")
    public ResponseEntity<List<ClassifiedPost>> getFilteredClassifieds(@RequestParam String statusType) {
        try {
            List<ClassifiedPost> posts = classifiedPostService.filterClassifiedPostsByStatus(statusType);
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ClassifiedPost> getClassifiedById(@PathVariable Integer postId) {
        try {
            ClassifiedPost post = classifiedPostService.getClassifiedById(postId);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{postId}/status")
    public ResponseEntity<ClassifiedPost> updateClassifiedStatus(
            @PathVariable Integer postId,
            @RequestBody Integer userId,
            @RequestBody String newStatus) {
        try {
            ClassifiedPost updatedPost = classifiedPostService.updateClassifiedPostStatus(postId, userId, newStatus);
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/subscriptions")
    public ResponseEntity<String> subscribe(@RequestBody Integer userId) {
        try {
            classifiedPostFeed.subscribe(userId);
            return new ResponseEntity<>("Subscribed successfully", HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/subscriptions")
    public ResponseEntity<String> unsubscribe(@RequestParam Integer userId) {
        try {
            classifiedPostFeed.unsubscribe(userId);
            return new ResponseEntity<>("Unsubscribed successfully", HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
