package com.hatchloom.connecthub.connecthub_service.service;

import com.hatchloom.connecthub.connecthub_service.dto.SendMessageRequest;
import com.hatchloom.connecthub.connecthub_service.model.Messages;
import com.hatchloom.connecthub.connecthub_service.repository.MessageRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageServiceTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private BaseUser sender;
    private BaseUser recipient;
    private BaseUser outsider;

    @BeforeEach
    void setup() {
        messageRepository.deleteAll();
        sender = new BaseUser(1, "Sender", "sender@gmail.com");
        recipient = new BaseUser(2, "Recipient", "recipient@gmail.com");
        outsider = new BaseUser(3, "Outsider", "outsider@gmail.com");
    }

    @Test
    @DisplayName("Test sending a message successfully")
    void testSendMessageSuccess() throws Exception {
        SendMessageRequest request = new SendMessageRequest(null, sender.id, "Hello how are you");

        mockMvc.perform(post("/api/message/{recipientId}/send", recipient.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf())
                .with(user(sender.name)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("conversationId").exists())
                .andExpect(jsonPath("messageId").exists())
                .andExpect(jsonPath("senderId").value(sender.id))
                .andExpect(jsonPath("recipientId").value(recipient.id))
                .andExpect(jsonPath("content").value("Hello how are you"));


        Messages savedMessage = messageRepository.findAll().getFirst();
        Assertions.assertEquals(1, messageRepository.count());
        Assertions.assertEquals(sender.id, savedMessage.getSenderId());
        Assertions.assertEquals("Hello how are you", savedMessage.getContent());
        Assertions.assertEquals(1, savedMessage.getConversation().getUser1Id());
        Assertions.assertEquals(2, savedMessage.getConversation().getUser2Id());
    }

    @Test
    @DisplayName("Test sending a message with missing content")
    void testSendMessageMissingContent() throws Exception {
        SendMessageRequest request = new SendMessageRequest(null, sender.id, "   ");

        mockMvc.perform(post("/api/message/{recipientId}/send", recipient.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf())
                .with(user(sender.name)))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(0, messageRepository.count());
    }

    @Test
    @DisplayName("Test sending a message with missing senderId")
    void testSendMessageMissingSenderId() throws Exception {
        SendMessageRequest request = new SendMessageRequest(null, null, "Hello");
        mockMvc.perform(post("/api/message/{recipientId}/send", recipient.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf())
                .with(user(sender.name)))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(0, messageRepository.count());
    }

    @Test
    @DisplayName("Test sending a message to self")
    void testSendMessageToSelf() throws Exception {
        SendMessageRequest request = new SendMessageRequest(null, sender.id, "Hello myself");
        mockMvc.perform(post("/api/message/{recipientId}/send", sender.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf())
                .with(user(sender.name)))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(0, messageRepository.count());
    }

    @Test
    @DisplayName("Test sending a message to non-existent conversation")
    void testSendMessageNonExistentConversation() throws Exception {
        SendMessageRequest request = new SendMessageRequest(999, sender.id, "Hello");
        mockMvc.perform(post("/api/message/{recipientId}/send", recipient.id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                        .with(user(sender.name)))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(0, messageRepository.count());
    }

    @Test
    @DisplayName("Test receiving a message where a user is not a participant in")
    void testGetMessageUserNotParticipant() throws Exception {
        SendMessageRequest request = new SendMessageRequest(null, sender.id, "Hello");
        mockMvc.perform(post("/api/message/{recipientId}/send", recipient.id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                        .with(user(sender.name)))
                .andExpect(status().isCreated());


        mockMvc.perform(get("/api/message/conversation/{conversationId}", 1)
                .param("userId", outsider.id.toString())
                .with(csrf())
                .with(user(outsider.name)))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(1, messageRepository.count());
    }

    @Test
    @DisplayName("Test sending a message where a user is not a participant in the conversation")
    void testSendMessageUserNotParticipantInConversation() throws Exception {
        SendMessageRequest request = new SendMessageRequest(null, sender.id, "Hello");
        mockMvc.perform(post("/api/message/{recipientId}/send", recipient.id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                        .with(user(sender.name)))
                .andExpect(status().isCreated());

        SendMessageRequest invalidRequest = new SendMessageRequest(1, outsider.id, "Hi");
        mockMvc.perform(post("/api/message/{recipientId}/send", recipient.id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .with(csrf())
                        .with(user(outsider.name)))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(1, messageRepository.count());
    }

    @Test
    @DisplayName("Test getting messages from a conversation successfully")
    void testGetConversationMessagesSuccess() throws Exception {
        SendMessageRequest request = new SendMessageRequest(null, sender.id, "Hello");
        mockMvc.perform(post("/api/message/{recipientId}/send", recipient.id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                        .with(user(sender.name)))
                .andExpect(status().isCreated());

        SendMessageRequest request2 = new SendMessageRequest(1, recipient.id, "Hi there");
        mockMvc.perform(post("/api/message/{recipientId}/send", sender.id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2))
                        .with(csrf())
                        .with(user(recipient.name)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/message/conversation/{conversationId}", 1)
                .param("userId", sender.id.toString())
                .with(csrf())
                .with(user(sender.name)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].conversationId").exists())
                .andExpect(jsonPath("$[0].senderId").value(sender.id))
                .andExpect(jsonPath("$[0].content").value("Hello"))
                        .andExpect(jsonPath("$[1].conversationId").exists())
                        .andExpect(jsonPath("$[1].senderId").value(recipient.id))
                        .andExpect(jsonPath("$[1].content").value("Hi there"));

        Assertions.assertEquals(2, messageRepository.count());
    }
}
