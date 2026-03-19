package com.hatchloom.connecthub.connecthub_service.service;

import com.hatchloom.connecthub.connecthub_service.model.Messages;
import com.hatchloom.connecthub.connecthub_service.repository.ConversationRepository;
import com.hatchloom.connecthub.connecthub_service.repository.MessageRepository;
import com.hatchloom.connecthub.connecthub_service.repository.ParticipantsRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private final NotificationService notificationService;
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final ParticipantsRepository participantsRepository;

    public MessageService(NotificationService notificationService,
                          MessageRepository messageRepository,
                          ConversationRepository conversationRepository,
                          ParticipantsRepository participantsRepository) {
        this.notificationService = notificationService;
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.participantsRepository = participantsRepository;
    }

    public Messages sendMessage(Integer conversationId, Integer senderId, String content) {
        return null;
    }
}
