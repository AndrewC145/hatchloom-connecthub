package com.hatchloom.connecthub.connecthub_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("announcement")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AnnouncementPost extends Post {
    @Column(name = "post_type", nullable = false, insertable = false, updatable = false)
    private final String postType = "announcement";

    public AnnouncementPost(String title, String content, Integer author) {
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
