package com.hatchloom.connecthub.connecthub_service.repository;

import com.hatchloom.connecthub.connecthub_service.model.ClassifiedPostApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing ClassifiedPostApplication entities
 */
public interface ClassifiedPostApplicationRepository extends JpaRepository<ClassifiedPostApplication, Integer> {
    boolean existsClassifiedPostApplicationByApplicantIdAndClassifiedPostId(Integer applicantId, Integer classifiedPostId);
    List<ClassifiedPostApplication> findByClassifiedPostIdOrderByAppliedAtDesc(Integer classifiedPostId);
    List<ClassifiedPostApplication> findByApplicantIdOrderByAppliedAtDesc(Integer applicantId);

    List<ClassifiedPostApplication> findByClassifiedPostId(Integer classifiedPostId);
}
