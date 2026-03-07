package com.hatchloom.connecthub.connecthub_service.service;

import com.hatchloom.connecthub.connecthub_service.dto.BasePostRequest;
import com.hatchloom.connecthub.connecthub_service.dto.PostCreationRequest;
import com.hatchloom.connecthub.connecthub_service.model.SharePost;
import com.hatchloom.connecthub.connecthub_service.repository.FeedPostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc

class FeedPostServiceTest {
    @Autowired
    private FeedPostService feedPostService;

    @Autowired
    private FeedPostRepository feedPostRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    static class BaseUser {
        public Integer id;
        public String name;
        public String email;

        public BaseUser(Integer id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
    }

    private BaseUser testUser;

    @BeforeEach
    void setup() throws Exception {
        feedPostRepository.deleteAll();
        testUser = new BaseUser(1, "Test User", "Test@gmail.com");
    }

    @Test
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
}
