package com.hatchloom.connecthub.connecthub_service.repository;
import com.hatchloom.connecthub.connecthub_service.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedPostRepository extends JpaRepository<Post, Integer> {
        Post getPostById(Integer id);

        void deletePostById(Integer id);
}
