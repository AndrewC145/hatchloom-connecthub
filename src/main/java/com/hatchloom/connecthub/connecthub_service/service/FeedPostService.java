package com.hatchloom.connecthub.connecthub_service.service;

import com.hatchloom.connecthub.connecthub_service.dto.PostCreationRequest;
import com.hatchloom.connecthub.connecthub_service.model.Post;
import com.hatchloom.connecthub.connecthub_service.repository.FeedPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedPostService {
    private final FeedPostRepository feedPostRepository;

    public FeedPostService(FeedPostRepository feedPostRepository) {
        this.feedPostRepository = feedPostRepository;
    }


    public Post createFeedPost(PostCreationRequest request) {
        return null;
    }
}
