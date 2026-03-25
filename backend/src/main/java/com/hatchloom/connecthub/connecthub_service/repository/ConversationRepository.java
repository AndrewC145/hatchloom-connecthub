package com.hatchloom.connecthub.connecthub_service.repository;

import com.hatchloom.connecthub.connecthub_service.model.Conversations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing Conversations entities
 */
public interface ConversationRepository extends JpaRepository<Conversations, UUID> {
    Optional<Conversations> findByUser1IdAndUser2Id(UUID user1Id, UUID user2Id);
}
