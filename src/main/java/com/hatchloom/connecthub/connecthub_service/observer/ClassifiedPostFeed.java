package com.hatchloom.connecthub.connecthub_service.observer;
import com.hatchloom.connecthub.connecthub_service.model.ClassifiedPost;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ClassifiedPostFeed implements ClassifiedSubject {
    private final Set<Integer> subscribers = ConcurrentHashMap.newKeySet();
    private final ClassifiedObserver observer;

    public ClassifiedPostFeed(ClassifiedObserver observer) {
        this.observer = observer;
    }

    @Override
    public void subscribe(Integer userId) {
        if (userId != null) {
            subscribers.add(userId);
        }
    }

    @Override
    public void unsubscribe(Integer userId) {
        if (userId != null) {
            subscribers.remove(userId);
        }
    }

    @Override
    public void notifyObservers(ClassifiedPost post, Integer creatorUserId) {
        for (Integer userId : subscribers) {
            if (!userId.equals(creatorUserId)) {
                observer.update(post, userId);
            }
        }
    }
}
