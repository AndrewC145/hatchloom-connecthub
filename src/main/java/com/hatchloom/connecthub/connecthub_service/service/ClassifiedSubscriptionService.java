package com.hatchloom.connecthub.connecthub_service.service;

import com.hatchloom.connecthub.connecthub_service.model.ClassifiedSubscription;
import com.hatchloom.connecthub.connecthub_service.repository.ClassifiedSubscriptionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClassifiedSubscriptionService {
    private final ClassifiedSubscriptionRepository subscriptionRepository;

    public ClassifiedSubscriptionService(ClassifiedSubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public void subscribe(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        if (!subscriptionRepository.existsByUserId(userId)) {
            ClassifiedSubscription subscription = new ClassifiedSubscription();
            subscription.setUserId(userId);
            subscriptionRepository.save(subscription);
        }
    }

    public void unsubscribe(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        subscriptionRepository.deleteByUserId(userId);
    }

    public List<Integer> getAllSubscribers() {
        return subscriptionRepository.findAllUserIds();
    }
}
