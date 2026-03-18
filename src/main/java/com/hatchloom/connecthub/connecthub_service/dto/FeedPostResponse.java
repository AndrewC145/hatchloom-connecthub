package com.hatchloom.connecthub.connecthub_service.dto;

import com.hatchloom.connecthub.connecthub_service.model.Post;

public record FeedPostResponse(
        Integer id,
        String title,
        String content,
        Integer author,
        String postType,
        String createdAt
) {
    public static FeedPostResponse from(Post p) {
        return new FeedPostResponse(
                p.getId(),
                p.getTitle(),
                p.getContent(),
                p.getAuthor(),
                p.getPostType(),
                p.getCreatedAt().toString()
        );
    }
}
