package com.hatchloom.connecthub.connecthub_service.service;

import com.hatchloom.connecthub.connecthub_service.dto.MessageResponse;
import com.hatchloom.connecthub.connecthub_service.dto.SendMessageResponse;
import com.hatchloom.connecthub.connecthub_service.model.Conversations;
import com.hatchloom.connecthub.connecthub_service.model.Messages;
import com.hatchloom.connecthub.connecthub_service.observer.MessageNotificationObserver;
import com.hatchloom.connecthub.connecthub_service.repository.ConversationRepository;
import com.hatchloom.connecthub.connecthub_service.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final MessageNotificationObserver messageNotificationObserver;
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    public MessageService(MessageNotificationObserver messageNotificationObserver,
                          MessageRepository messageRepository,
                          ConversationRepository conversationRepository) {
        this.messageNotificationObserver = messageNotificationObserver;
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
    }

    @Transactional
    public SendMessageResponse sendMessage(Integer conversationId, Integer senderId, Integer recipientId, String content) {
        if (!validateInputs(senderId, recipientId, content)) {
            throw new IllegalArgumentException("Invalid input: senderId, recipientId, and content must be provided.");
        }

        Conversations c;
        if (conversationId == null) {
            c = getOrCreateConversation(senderId, recipientId);
        }
        else {
            c = conversationRepository.findById(conversationId).orElseThrow(() -> new IllegalArgumentException("Conversation not found with id: " + conversationId));
        }

        Integer lesserId = Math.min(senderId, recipientId);
        Integer greaterId = Math.max(senderId, recipientId);

        if (!(c.getUser1Id().equals(lesserId) && c.getUser2Id().equals(greaterId))) {
            throw new IllegalArgumentException("Conversation does not match sender and recipient");
        }

        Messages m = new Messages();
        m.setConversation(c);
        m.setSenderId(senderId);
        m.setContent(content.trim());
        Messages savedMessage = messageRepository.save(m);

        messageNotificationObserver.update(savedMessage, recipientId);
        return new SendMessageResponse(c.getId(), savedMessage.getId(), lesserId, greaterId, savedMessage.getContent(), savedMessage.getCreatedAt());
    }

    public Conversations getOrCreateConversation(Integer senderId, Integer recipientId) {
        Integer lesserId = Math.min(senderId, recipientId);
        Integer greaterId = Math.max(senderId, recipientId);
        Optional<Conversations> c = conversationRepository.findByUser1IdAndUser2Id(lesserId, greaterId);

        if (c.isEmpty()) {
            Conversations conversation = new Conversations();
            conversation.setUser1Id(lesserId);
            conversation.setUser2Id(greaterId);
            return conversationRepository.save(conversation);
        } else {
            return c.get();
        }
    }

    public boolean validateInputs(Integer senderId, Integer recipientId, String content) {
        return senderId != null && recipientId != null && !senderId.equals(recipientId) && content != null && !content.trim().isEmpty();
    }

    public List<MessageResponse> getConversationMessages(Integer conversationId, Integer userId) {
        Conversations conversation = conversationRepository.findById(conversationId).orElseThrow(() -> new IllegalArgumentException("Conversation not found with id: " + conversationId));

        if (!conversation.getUser1Id().equals(userId) && !conversation.getUser2Id().equals(userId)) {
            throw new IllegalArgumentException("User is not a participant in this conversation");
        }

        List<Messages> messages = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
        return messages.stream()
                .map(m -> new MessageResponse(m.getId(), m.getConversationId(), m.getSenderId(), m.getContent(), m.getCreatedAt()))
                .collect(Collectors.toList());

    }
}
