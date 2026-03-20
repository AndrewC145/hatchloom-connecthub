package com.hatchloom.connecthub.connecthub_service.observer;
import com.hatchloom.connecthub.connecthub_service.model.ClassifiedPost;
import com.hatchloom.connecthub.connecthub_service.service.ClassifiedSubscriptionService;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class ClassifiedPostFeed implements ClassifiedSubject {
    private final ClassifiedSubscriptionService subscriptionService;
    private final ClassifiedObserver observer;

    public ClassifiedPostFeed(ClassifiedSubscriptionService subscriptionService, ClassifiedObserver observer) {
        this.subscriptionService = subscriptionService;
        this.observer = observer;
    }

    @Override
    public void subscribe(Integer userId) {
        subscriptionService.subscribe(userId);
    }

    @Override
    public void unsubscribe(Integer userId) {
        subscriptionService.unsubscribe(userId);
    }

    @Override
    public void notifyObservers(ClassifiedPost post, Integer creatorUserId) {
        List<Integer> subscribers = subscriptionService.getAllSubscribers();

        for (Integer userId : subscribers) {
            if (!userId.equals(creatorUserId)) {
                observer.update(post, userId);
            }
        }
    }
}
