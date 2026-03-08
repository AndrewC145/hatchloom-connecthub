package com.hatchloom.connecthub.connecthub_service.service;

import com.hatchloom.connecthub.connecthub_service.dto.BasePostRequest;
import com.hatchloom.connecthub.connecthub_service.dto.CommentRequest;
import com.hatchloom.connecthub.connecthub_service.dto.LikeRequest;
import com.hatchloom.connecthub.connecthub_service.dto.PostCreationRequest;
import com.hatchloom.connecthub.connecthub_service.model.AnnouncementPost;
import com.hatchloom.connecthub.connecthub_service.model.FeedAction;
import com.hatchloom.connecthub.connecthub_service.repository.FeedActionRepository;
import com.hatchloom.connecthub.connecthub_service.repository.FeedPostRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FeedActionServiceTest {
    @Autowired
    private FeedPostRepository feedPostRepository;

    @Autowired
    private FeedActionRepository feedActionRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private BaseUser testUser;
    private BaseUser anotherUser;
    private AnnouncementPost post;

    @BeforeEach
    void setup() throws Exception {
        feedActionRepository.deleteAll();
        testUser = new BaseUser(1, "testuser", "test@gmail.com");
        anotherUser = new BaseUser(2, "anotheruser", "anotheruser@gmail.com");

        PostCreationRequest request = new PostCreationRequest(
                new BasePostRequest("Test Announcement", "This is a test announcement post", testUser.id),
                "announcement"
        );

        mockMvc.perform(post("/api/feed")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf())
                .with(user("testuser")))
                .andExpect(status().isCreated());

        post = (AnnouncementPost) feedPostRepository.findAll().getFirst();
    }

    @Test
    @DisplayName("Test liking a post")
    @Transactional
    void testLikePost() throws Exception {
        LikeRequest dto = new LikeRequest(anotherUser.id, post.getId());
        mockMvc.perform(post("/api/feed/actions/like")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf())
                .with(user("anotheruser")))
                .andExpect(status().isCreated());

        FeedAction like = feedActionRepository.findByPostIdAndUserIdAndActionType(post.getId(), anotherUser.id, "like").orElse(null);
        Assertions.assertNotNull(like);
    }

    @Test
    @DisplayName("Test liking a post twice")
    @Transactional
    void testLikePostTwice() throws Exception {
        LikeRequest dto = new LikeRequest(anotherUser.id, post.getId());
        mockMvc.perform(post("/api/feed/actions/like")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf())
                .with(user("anotheruser")))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/feed/actions/like")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf())
                .with(user("anotheruser")))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(1, feedPostRepository.count());
    }

    @Test
    @DisplayName("Test liking a non-existent post")
    @Transactional
    void testLikeNonExistentPost() throws Exception {
        LikeRequest dto = new LikeRequest(anotherUser.id, 999);
        mockMvc.perform(post("/api/feed/actions/like")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf())
                .with(user("anotheruser")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test unliking a post")
    @Transactional
    void testUnlikePost() throws Exception {
        LikeRequest dto = new LikeRequest(anotherUser.id, post.getId());
        mockMvc.perform(post("/api/feed/actions/like")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf())
                .with(user("anotheruser")))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/api/feed/actions/like")
                .param("postId", String.valueOf(post.getId()))
                .param("userId", String.valueOf(anotherUser.id))
                .with(csrf())
                .with(user("anotheruser")))
                .andExpect(status().isOk());

        FeedAction like = feedActionRepository.findByPostIdAndUserIdAndActionType(post.getId(), anotherUser.id, "like").orElse(null);
        Assertions.assertNull(like);
    }

    @Test
    @DisplayName("Test unliking a post that was not liked")
    @Transactional
    void testUnlikePostNotLiked() throws Exception {
        mockMvc.perform(delete("/api/feed/actions/like")
                        .param("postId", String.valueOf(post.getId()))
                        .param("userId", String.valueOf(anotherUser.id))
                        .with(csrf())
                        .with(user("anotheruser")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test unliking a non-existent post")
    @Transactional
    void testUnlikeNonExistentPost() throws Exception {
        mockMvc.perform(delete("/api/feed/actions/like")
                        .param("postId", "999")
                        .param("userId", String.valueOf(anotherUser.id))
                        .with(csrf())
                        .with(user("anotheruser")))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Test adding a comment")
    @Transactional
    void testAddComment() throws Exception {
        CommentRequest dto = new CommentRequest(anotherUser.id, post.getId(), "This is a test comment");
        mockMvc.perform(post("/api/feed/actions/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                        .with(user("anotheruser")))
                .andExpect(status().isCreated());

        FeedAction comment = feedActionRepository.findByPostIdAndUserIdAndActionType(post.getId(), anotherUser.id, "comment").orElse(null);
        Assertions.assertNotNull(comment);
        Assertions.assertEquals("This is a test comment", comment.getCommentText());
    }

    @Test
    @DisplayName("Test adding an empty comment")
    @Transactional
    void testAddEmptyComment() throws Exception {
        CommentRequest dto = new CommentRequest(anotherUser.id, post.getId(), "");
        mockMvc.perform(post("/api/feed/actions/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                        .with(user("anotheruser")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test adding a comment to a non-existent post")
    @Transactional
    void testAddCommentNonExistentPost() throws Exception {
        CommentRequest dto = new CommentRequest(anotherUser.id, 999, "This is a test comment");
        mockMvc.perform(post("/api/feed/actions/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                        .with(user("anotheruser")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test deleting a comment")
    @Transactional
    void testDeleteComment() throws Exception {
        CommentRequest dto = new CommentRequest(anotherUser.id, post.getId(), "This is a test comment");
        mockMvc.perform(post("/api/feed/actions/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                        .with(user("anotheruser")))
                .andExpect(status().isCreated());

        FeedAction comment = feedActionRepository.findByPostIdAndUserIdAndActionType(post.getId(), anotherUser.id, "comment").orElse(null);
        Assertions.assertNotNull(comment);

        mockMvc.perform(delete("/api/feed/actions/comment/{commentId}", comment.getId())
                .param("userId", String.valueOf(anotherUser.id))
                .with(csrf())
                .with(user("anotheruser")))
                .andExpect(status().isOk());

        FeedAction deletedComment = feedActionRepository.findByIdAndUserIdAndActionType(comment.getId(), anotherUser.id, "comment").orElse(null);
        Assertions.assertNull(deletedComment);
    }

    @Test
    @DisplayName("Test deleting an unauthorized comment")
    @Transactional
    void testDeleteUnauthorizedComment() throws Exception {
        CommentRequest dto = new CommentRequest(anotherUser.id, post.getId(), "This is a test comment");
        mockMvc.perform(post("/api/feed/actions/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                        .with(user("anotheruser")))
                .andExpect(status().isCreated());

        FeedAction comment = feedActionRepository.findByPostIdAndUserIdAndActionType(post.getId(), anotherUser.id, "comment").orElse(null);
        Assertions.assertNotNull(comment);

        mockMvc.perform(delete("/api/feed/actions/comment/{commentId}", comment.getId())
                .param("userId", String.valueOf(999))
                .with(csrf())
                .with(user("otheruser")))
                .andExpect(status().isBadRequest());

        FeedAction existingComment = feedActionRepository.findByIdAndUserIdAndActionType(comment.getId(), anotherUser.id, "comment").orElse(null);
        Assertions.assertNotNull(existingComment);
    }

    @Test
    @DisplayName("Test liking a comment")
    @Transactional
    void testLikeComment() throws Exception {
        CommentRequest dto = new CommentRequest(anotherUser.id, post.getId(), "This is a test comment");
        mockMvc.perform(post("/api/feed/actions/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                        .with(user("anotheruser")))
                .andExpect(status().isCreated());

        FeedAction comment = feedActionRepository.findByPostIdAndUserIdAndActionType(post.getId(), anotherUser.id, "comment").orElse(null);
        Assertions.assertNotNull(comment);
        Assertions.assertEquals("This is a test comment", comment.getCommentText());


        mockMvc.perform(post("/api/feed/actions/comment/{commentId}/like", comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                .param("userId", String.valueOf(testUser.id))
                .with(csrf())
                .with(user("testuser")))
                .andExpect(status().isCreated());

        FeedAction like = feedActionRepository.findByParentActionIdAndUserIdAndActionType(comment.getId(), testUser.id, "like").orElse(null);
        Assertions.assertNotNull(like);
    }

    @Test
    @DisplayName("Test liking a comment twice")
    @Transactional
    void testLikeCommentTwice() throws Exception {
        CommentRequest dto = new CommentRequest(anotherUser.id, post.getId(), "This is a test comment");
        mockMvc.perform(post("/api/feed/actions/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                        .with(user("anotheruser")))
                .andExpect(status().isCreated());

        FeedAction comment = feedActionRepository.findByPostIdAndUserIdAndActionType(post.getId(), anotherUser.id, "comment").orElse(null);
        Assertions.assertNotNull(comment);

        mockMvc.perform(post("/api/feed/actions/comment/{commentId}/like", comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                .param("userId", String.valueOf(testUser.id))
                .with(csrf())
                .with(user("testuser")))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/feed/actions/comment/{commentId}/like", comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                .param("userId", String.valueOf(testUser.id))
                .with(csrf())
                .with(user("testuser")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test liking a non-existent comment")
    @Transactional
    void testLikeNonExistentComment() throws Exception {
        mockMvc.perform(post("/api/feed/actions/comment/{commentId}/like", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId", String.valueOf(testUser.id))
                        .with(csrf())
                        .with(user("testuser")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test unliking a comment")
    @Transactional
    void testUnlikeComment() throws Exception {
        CommentRequest dto = new CommentRequest(anotherUser.id, post.getId(), "This is a test comment");
        mockMvc.perform(post("/api/feed/actions/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                        .with(user("anotheruser")))
                .andExpect(status().isCreated());

        FeedAction comment = feedActionRepository.findByPostIdAndUserIdAndActionType(post.getId(), anotherUser.id, "comment").orElse(null);
        Assertions.assertNotNull(comment);

        mockMvc.perform(post("/api/feed/actions/comment/{commentId}/like", comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId", String.valueOf(testUser.id))
                        .with(csrf())
                        .with(user("testuser")))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/api/feed/actions/comment/{commentId}/like", comment.getId()
        ).contentType(MediaType.APPLICATION_JSON)
                .param("userId", String.valueOf(testUser.id))
                .with(csrf())
                .with(user("testuser"))
                ).andExpect(status().isOk());
        FeedAction like = feedActionRepository.findByParentActionIdAndUserIdAndActionType(comment.getId(), testUser.id, "like").orElse(null);
        Assertions.assertNull(like);
    }

    @Test
    @DisplayName("Test unliking a comment that was not liked")
    @Transactional
    void testUnlikeCommentNotLiked() throws Exception {
        CommentRequest dto = new CommentRequest(anotherUser.id, post.getId(), "This is a test comment");
        mockMvc.perform(post("/api/feed/actions/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                        .with(user("anotheruser")))
                .andExpect(status().isCreated());

        FeedAction comment = feedActionRepository.findByPostIdAndUserIdAndActionType(post.getId(), anotherUser.id, "comment").orElse(null);
        Assertions.assertNotNull(comment);

        mockMvc.perform(delete("/api/feed/actions/comment/{commentId}/like", comment.getId()
        ).contentType(MediaType.APPLICATION_JSON)
                .param("userId", String.valueOf(testUser.id))
                .with(csrf())
                .with(user("testuser"))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test getting comment likes for a comment")
    @Transactional
    void testGetCommentLikes() throws Exception {
        CommentRequest dto = new CommentRequest(anotherUser.id, post.getId(), "This is a test comment");
        mockMvc.perform(post("/api/feed/actions/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                        .with(user("anotheruser")))
                .andExpect(status().isCreated());

        FeedAction comment = feedActionRepository.findByPostIdAndUserIdAndActionType(post.getId(), anotherUser.id, "comment").orElse(null);
        Assertions.assertNotNull(comment);

        mockMvc.perform(post("/api/feed/actions/comment/{commentId}/like", comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId", String.valueOf(testUser.id))
                        .with(csrf())
                        .with(user("testuser")))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/feed/actions/comment/{commentId}/likes/count", comment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(user("testuser")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
    }
}
