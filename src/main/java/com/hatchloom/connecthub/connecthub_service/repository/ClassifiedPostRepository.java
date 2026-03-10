package com.hatchloom.connecthub.connecthub_service.repository;
import com.hatchloom.connecthub.connecthub_service.model.ClassifiedPost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface ClassifiedPostRepository extends JpaRepository<ClassifiedPost, Integer> {
    Optional<ClassifiedPost> getClassifiedPostById(Integer id);

    List<ClassifiedPost> findByStatus(String status);
}
