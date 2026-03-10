package com.hatchloom.connecthub.connecthub_service.service;

import com.hatchloom.connecthub.connecthub_service.dto.BasePostRequest;
import com.hatchloom.connecthub.connecthub_service.dto.PostCreationRequest;
import com.hatchloom.connecthub.connecthub_service.model.Post;
import com.hatchloom.connecthub.connecthub_service.model.SharePost;
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

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class FeedPostServiceTest {
    @Autowired
    private FeedPostRepository feedPostRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private BaseUser testUser;

    @BeforeEach
    void setup() {
        feedPostRepository.deleteAll();
        testUser = new BaseUser(1, "Test User", "Test@gmail.com");
    }

    @Test
    @DisplayName("Test creating a new feed post successfully")
    void testCreateFeedPost() throws Exception {
        PostCreationRequest dto = new PostCreationRequest(new BasePostRequest("Test Post 1", "This is a test post", testUser.id), "share");

        mockMvc.perform(post("/api/feed")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf())
                .with(user("testuser")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Post 1"))
                .andExpect(jsonPath("$.content").value("This is a test post"))
                .andExpect(jsonPath("$.author").value(testUser.id));

        Assertions.assertEquals(1, feedPostRepository.count());
        SharePost post = (SharePost) feedPostRepository.getPostById(1);

        Assertions.assertEquals("Test Post 1", post.getTitle());
        Assertions.assertEquals("This is a test post", post.getContent());
        Assertions.assertEquals(testUser.id, post.getAuthor());
    }

    @Test
    @DisplayName("Test creating a feed post with missing title")
    void createFeedPost_MissingTitle() throws Exception {
        PostCreationRequest dto = new PostCreationRequest(new BasePostRequest("", "This is a test post", testUser.id), "share");

        mockMvc.perform(post("/api/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                        .with(user("testuser")))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(0, feedPostRepository.count());
    }

    @Test
    @DisplayName("Test creating a feed post with missing content and author")
    void createFeedPost_MissingOtherInformation() throws Exception {
        PostCreationRequest dto = new PostCreationRequest(new BasePostRequest("Test Post 3", "", null), "share");

        mockMvc.perform(post("/api/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                        .with(user("testuser")))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(0, feedPostRepository.count());
    }

    @Test
    @DisplayName("Test create different types of feed posts")
    void createFeedPost_DifferentTypes() throws Exception {
        String[] postTypes = {"share", "announcement", "achievement"};
        for (String post : postTypes) {
            PostCreationRequest dto = new PostCreationRequest(new BasePostRequest("Test " + post, "This is a test " + post, testUser.id), post);

            mockMvc.perform(post("/api/feed")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(csrf())
                            .with(user("testuser")))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.title").value("Test " + post))
                    .andExpect(jsonPath("$.content").value("This is a test " + post))
                    .andExpect(jsonPath("$.author").value(testUser.id));
        }

        Assertions.assertEquals(3, feedPostRepository.count());
    }

    @Test
    @DisplayName("Test creating a feed post with invalid post type")
    void createFeedPost_InvalidPostType() throws Exception {
        PostCreationRequest dto = new PostCreationRequest(new BasePostRequest("Test Invalid", "This is a test post with invalid type", testUser.id), "invalidType");

        mockMvc.perform(post("/api/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                        .with(user("testuser")))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(0, feedPostRepository.count());
    }

    @Test
    @DisplayName("Test creating a feed post with null post type")
    void createFeedPost_NullPostType() throws Exception {
        PostCreationRequest dto = new PostCreationRequest(new BasePostRequest("Test Null Type", "This is a test post with null type", testUser.id), null);

        mockMvc.perform(post("/api/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                        .with(user("testuser")))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(0, feedPostRepository.count());
    }

    @Test
    @Transactional
    @DisplayName("Test deleting a feed post successfully")
    void deleteFeedPost() throws Exception {
        PostCreationRequest dto = new PostCreationRequest(new BasePostRequest("Test Post to Delete", "This post will be deleted", testUser.id), "share");
        mockMvc.perform(post("/api/feed")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf())
                .with(user("testuser")))
                .andExpect(status().isCreated());

        List<Post> posts = feedPostRepository.findAll();
        Post post = posts.getFirst();

        mockMvc.perform(delete("/api/feed/{postId}", post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", String.valueOf(testUser.id))
                .with(csrf())
                .with(user("testuser")))
                .andExpect(status().isOk())
                .andExpect(content().string("Post deleted successfully"));

        Assertions.assertEquals(0, feedPostRepository.count());
    }

    @Test
    @Transactional
    @DisplayName("Test deleting a post with invalid post Id")
    void deleteFeedPost_Invalid_PostId() throws Exception {
        PostCreationRequest dto = new PostCreationRequest(new BasePostRequest("Test Post to Delete", "This post will be deleted", testUser.id), "share");
        mockMvc.perform(post("/api/feed")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf())
                .with(user("testuser")))
                .andExpect(status().isCreated());


        mockMvc.perform(delete("/api/feed/{postId}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .param("userId", String.valueOf(testUser.id))
                .with(csrf())
                .with(user("testuser")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Post with ID 999 does not exist"));

        Assertions.assertEquals(1, feedPostRepository.count());
    }

    @Test
    @DisplayName("Test deleting a feed post with invalid user")
    void deleteFeedPost_InvalidUser() throws Exception {
        PostCreationRequest dto = new PostCreationRequest(new BasePostRequest("Test Post to Delete", "This post will be deleted", testUser.id), "share");
        mockMvc.perform(post("/api/feed")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf())
                .with(user("testuser")))
                .andExpect(status().isCreated());

        List<Post> posts = feedPostRepository.findAll();
        Post post = posts.getFirst();

        mockMvc.perform(delete("/api/feed/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId", String.valueOf(testUser.id + 1))
                        .with(csrf())
                        .with(user("testuser")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User " + (testUser.id + 1) + " is not authorized to delete post " + post.getId()));

        Assertions.assertEquals(1, feedPostRepository.count());
    }
}
