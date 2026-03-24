package com.hatchloom.connecthub.connecthub_service.repository;

import com.hatchloom.connecthub.connecthub_service.model.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for managing Messages entities
 */
public interface MessageRepository extends JpaRepository<Messages, Integer> {
    @Query("SELECT m FROM Messages m WHERE m.conversation.id = :conversationId ORDER BY m.createdAt ASC")
    List<Messages> findByConversationIdOrderByCreatedAtAsc(Integer conversationId);
}
