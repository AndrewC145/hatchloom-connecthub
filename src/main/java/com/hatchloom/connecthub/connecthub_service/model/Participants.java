package com.hatchloom.connecthub.connecthub_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "participants", uniqueConstraints = {@UniqueConstraint(name = "unique_participant",
columnNames = {"conversation_id", "user_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Participants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversations conversation;

    @Column(name = "conversation_id", nullable = false, insertable = false, updatable = false)
    private Integer conversationId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_read_message_id")
    private Messages lastReadMessage;

    @Transient
    public Integer getLastReadMessageId() {
        return lastReadMessage != null ? lastReadMessage.getId() : null;
    }

    @Transient
    public Integer getConversationId() {
        return conversation != null ? conversation.getId() : null;
    }
}
