package com.hatchloom.connecthub.connecthub_service.controller;

import com.hatchloom.connecthub.connecthub_service.dto.ClassifiedPostCreationRequest;
import com.hatchloom.connecthub.connecthub_service.model.ClassifiedPost;
import com.hatchloom.connecthub.connecthub_service.service.ClassifiedPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classified")
public class ClassifiedPostController {
    private final ClassifiedPostService classifiedPostService;

    public ClassifiedPostController(ClassifiedPostService classifiedPostService) {
        this.classifiedPostService = classifiedPostService;
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
            @RequestParam Integer userId,
            @RequestParam String newStatus) {
        try {
            ClassifiedPost updatedPost = classifiedPostService.updateClassifiedPostStatus(postId, userId, newStatus);
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


}
