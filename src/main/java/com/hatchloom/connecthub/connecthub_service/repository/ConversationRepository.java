package com.hatchloom.connecthub.connecthub_service.repository;

import com.hatchloom.connecthub.connecthub_service.model.Conversations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversations, Integer> {
    Optional<Conversations> findByUser1IdAndUser2Id(Integer user1Id, Integer user2Id);
}
