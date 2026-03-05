package com.hatchloom.connecthub.connecthub_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AchievementPost extends Post {

    @Column(name = "post_type", nullable = false)
    private final String postType = "achievement";

    public AchievementPost(String title, String content, Integer author) {
        super();
        this.setTitle(title);
        this.setContent(content);
        this.setAuthor(author);
    }

    @Override
    public String getPostType() {
        return postType;
    }
}
