package com.hatchloom.connecthub.connecthub_service.repository;

import com.hatchloom.connecthub.connecthub_service.model.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Messages, Integer> {
    List<Messages> findByConversationIdOrderByCreatedAtAsc(Integer conversationId);
}
