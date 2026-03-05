package com.hatchloom.connecthub.connecthub_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "posts")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SharePost extends Post {

    @Column(name = "post_type", nullable = false)
    private final String postType = "share";

    public SharePost(String title, String content, Integer author) {
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

