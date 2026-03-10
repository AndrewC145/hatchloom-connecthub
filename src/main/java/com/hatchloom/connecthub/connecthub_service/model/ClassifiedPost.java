package com.hatchloom.connecthub.connecthub_service.model;

import com.hatchloom.connecthub.connecthub_service.enums.ClassifiedStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "classified_posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassifiedPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Integer author;

    @Column(name = "project_id", nullable = false)
    private Integer projectId;

    @Column(nullable = false, length = 20)
    private String status = String.valueOf(ClassifiedStatus.OPEN);

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

