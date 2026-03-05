package com.hatchloom.connecthub.connecthub_service.repository;
import com.hatchloom.connecthub.connecthub_service.model.FeedPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFeedRepository extends JpaRepository<FeedPost, Integer> {
        FeedPost getFeedPostById(Integer id);
}
