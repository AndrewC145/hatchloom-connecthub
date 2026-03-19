package com.hatchloom.connecthub.connecthub_service.service;

import com.hatchloom.connecthub.connecthub_service.model.Conversations;
import com.hatchloom.connecthub.connecthub_service.model.Messages;
import com.hatchloom.connecthub.connecthub_service.repository.ConversationRepository;
import com.hatchloom.connecthub.connecthub_service.repository.MessageRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private final NotificationService notificationService;
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    public MessageService(NotificationService notificationService,
                          MessageRepository messageRepository,
                          ConversationRepository conversationRepository) {
        this.notificationService = notificationService;
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
    }

    public Messages sendMessage(Integer conversationId, Integer senderId, String content) {
        return null;
    }

    public Conversations createConversation(Integer )
}
