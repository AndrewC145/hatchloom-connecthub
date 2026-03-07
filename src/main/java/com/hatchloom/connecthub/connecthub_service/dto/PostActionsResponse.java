package com.hatchloom.connecthub.connecthub_service.dto;

import java.util.List;

public record PostActionsResponse(
        Integer postId,
        Long likesCount,
        Long commentsCount,
        List<CommentResponse> comments,
        boolean isLikedByCurrentUser
) {
}

