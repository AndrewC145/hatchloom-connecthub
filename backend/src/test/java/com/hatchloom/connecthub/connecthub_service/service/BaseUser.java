package com.hatchloom.connecthub.connecthub_service.service;

public class BaseUser {
    protected Integer id;
    protected String name;
    protected String email;

    public BaseUser(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
