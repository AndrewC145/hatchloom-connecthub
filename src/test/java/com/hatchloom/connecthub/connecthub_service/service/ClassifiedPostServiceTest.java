package com.hatchloom.connecthub.connecthub_service.service;

import com.hatchloom.connecthub.connecthub_service.dto.BasePostRequest;
import com.hatchloom.connecthub.connecthub_service.dto.ClassifiedPostCreationRequest;
import com.hatchloom.connecthub.connecthub_service.model.ClassifiedPost;
import com.hatchloom.connecthub.connecthub_service.repository.ClassifiedPostRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClassifiedPostServiceTest {
    @Autowired
    ClassifiedPostRepository classifiedPostRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private BaseUser testUser;
    private BaseProject testProject;

    @BeforeEach
    void setup() {
        classifiedPostRepository.deleteAll();
        testUser = new BaseUser(1, "testuser", "test@gmail.com");
        testProject = new BaseProject(1, "Test Project", "A project for testing", testUser, List.of());
    }

    @Test
    @DisplayName("Test creating a classified post with valid data")
    void testCreateClassifiedPost() throws Exception {
        ClassifiedPostCreationRequest dto = new ClassifiedPostCreationRequest(new BasePostRequest("Classified test post",
                "This is a classified test", testUser.id), testProject.id, "open");

        mockMvc.perform(post("/api/classified")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf())
                .with(user("testuser")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Classified test post"))
                .andExpect(jsonPath("$.content").value("This is a classified test"))
                .andExpect(jsonPath("$.author").value(testUser.id))
                .andExpect(jsonPath("$.projectId").value(testProject.id))
                .andExpect(jsonPath("$.status").value("open"));

        ClassifiedPost post = classifiedPostRepository.findAll().get(0);
        Assertions.assertEquals(1, classifiedPostRepository.count());
        Assertions.assertEquals(post.getProjectId(), testProject.id);
        Assertions.assertEquals(post.getAuthor(), testUser.id);
    }

    @Test
    @DisplayName("Test creating a classified post with invalid status")
    void testCreateClassifiedPostInvalidStatus() throws Exception {
        ClassifiedPostCreationRequest dto = new ClassifiedPostCreationRequest(new BasePostRequest("Classified test post",
                "This is a classified test", testUser.id), testProject.id, "invalid_status");

        mockMvc.perform(post("/api/classified")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf())
                .with(user("testuser")))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(0, classifiedPostRepository.count());
    }

    @Test
    @DisplayName("Test creating a classified post with null fields")
    void testCreateClassifiedPostNullFields() throws Exception {
        ClassifiedPostCreationRequest dto = new ClassifiedPostCreationRequest(new BasePostRequest("", "", null), null, "open");

        mockMvc.perform(post("/api/classified")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf())
                .with(user("testuser")))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(0, classifiedPostRepository.count());
    }

    @Test
    @DisplayName("Test creating a classified post with long title")
    void testCreateClassifiedPostLongTitle() throws Exception {
        ClassifiedPostCreationRequest dto = new ClassifiedPostCreationRequest(new BasePostRequest("A".repeat(300),
                "This is a classified test", testUser.id), testProject.id, "open");

        mockMvc.perform(post("/api/classified")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf())
                .with(user("testuser")))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(0, classifiedPostRepository.count());
    }

    @Test
    @DisplayName("Test creating a classified post with long content")
    void testCreateClassifiedPostLongContent() throws Exception {
        ClassifiedPostCreationRequest dto = new ClassifiedPostCreationRequest(new BasePostRequest("Classified test post",
                "A".repeat(3001), testUser.id), testProject.id, "open");

        mockMvc.perform(post("/api/classified")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf())
                .with(user("testuser")))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(0, classifiedPostRepository.count());
    }

    @Test
    @DisplayName("Test get filtered classifeid posts")
    void testFilterClassifiedPosts() throws Exception {
        String[] statuses = {"open", "open", "open", "filled", "closed"};

        for (String status : statuses) {
            ClassifiedPostCreationRequest dto = new ClassifiedPostCreationRequest(new BasePostRequest("Classified test post",
                    "This is a classified test", testUser.id), testProject.id, status);

            mockMvc.perform(post("/api/classified")
            .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto))
                    .with(csrf())
                    .with(user("testuser")))
                    .andExpect(status().isCreated());
        }

        mockMvc.perform(get("/api/classified/filtered")
                .param("statusType", "open")
                        .with(csrf())
                .with(user("testuser")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));

        mockMvc.perform(get("/api/classified/filtered")
                .param("statusType", "invalid")
                .with(csrf())
                .with(user("testuser")))
                        .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/classified/filtered")
                        .param("statusType", "filled")
                        .with(csrf())
                        .with(user("testuser")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        Assertions.assertEquals(5, classifiedPostRepository.count());
    }

    @Test
    @DisplayName("Test update classified post status")
    void testUpdateClassifiedPostStatus() throws Exception {
        ClassifiedPostCreationRequest dto = new ClassifiedPostCreationRequest(new BasePostRequest("Classified test post",
                "This is a classified test", testUser.id), testProject.id, "open");

        String response = mockMvc.perform(post("/api/classified")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf())
                .with(user("testuser")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        ClassifiedPost createdPost = objectMapper.readValue(response, ClassifiedPost.class);

        mockMvc.perform(put("/api/classified/{postId}/status", createdPost.getId())
                .param("userId", String.valueOf(testUser.id))
                .param("newStatus", "filled")
                .with(csrf())
                .with(user("testuser")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("filled"));

        ClassifiedPost updatedPost = classifiedPostRepository.findById(createdPost.getId()).orElseThrow();
        Assertions.assertEquals("filled", updatedPost.getStatus());
    }

    @Test
    @DisplayName("Test update classified post status with a post that doesn't exist")
    void testUpdateClassifiedPostStatusInvalidStatus() throws Exception {
        ClassifiedPostCreationRequest dto = new ClassifiedPostCreationRequest(new BasePostRequest("Classified test post",
                "This is a classified test", testUser.id), testProject.id, "open");

        mockMvc.perform(post("/api/classified")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf())
                .with(user("testuser")))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/api/classified/{postId}/status", 999)
                        .param("userId", String.valueOf(testUser.id))
                        .param("newStatus", "filled")
                        .with(csrf())
                        .with(user("testuser")))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(1, classifiedPostRepository.count());
        ClassifiedPost post = classifiedPostRepository.findAll().getFirst();
        Assertions.assertEquals("open", post.getStatus());
    }

}
