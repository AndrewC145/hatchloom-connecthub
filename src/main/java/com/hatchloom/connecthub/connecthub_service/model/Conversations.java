package com.hatchloom.connecthub.connecthub_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversations", uniqueConstraints = {@UniqueConstraint(name = "unique_conversation",
        columnNames = {"user1_id", "user2_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conversations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user1_id", nullable = false)
    private Integer user1Id;

    @Column(name = "user2_id", nullable = false)
    private Integer user2Id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
