package com.example.devtask_backend.project;

public class ProjectRequest {
    private String title;
    private String description;

    public ProjectRequest() {}

    public ProjectRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
