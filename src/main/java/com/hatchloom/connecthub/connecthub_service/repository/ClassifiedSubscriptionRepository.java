package com.hatchloom.connecthub.connecthub_service.repository;

import com.hatchloom.connecthub.connecthub_service.model.ClassifiedSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for managing ClassifiedSubscription entities
 */
public interface ClassifiedSubscriptionRepository extends JpaRepository<ClassifiedSubscription, Integer> {
    @Query("SELECT s.userId FROM ClassifiedSubscription s")
    List<Integer> findAllUserIds();

    boolean existsByUserId(Integer userId);
    void deleteByUserId(Integer userId);
}
