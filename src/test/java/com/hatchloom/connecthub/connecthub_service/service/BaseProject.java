package com.hatchloom.connecthub.connecthub_service.service;

import com.hatchloom.connecthub.connecthub_service.model.ClassifiedPost;

import java.util.List;

public class BaseProject {
    protected Integer id;
    protected String name;
    protected String description;
    protected BaseUser owner;
    protected List<ClassifiedPost> classifiedPosts;

    public BaseProject(Integer id, String name, String description, BaseUser owner, List<ClassifiedPost> classifiedPosts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.classifiedPosts = classifiedPosts;
    }
}
