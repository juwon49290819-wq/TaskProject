package com.example.devtask_backend.project;

public class ProjectResponse {
    private Long id;
    private String title;
    private String description;
    private String username;

    public ProjectResponse(Long id, String title, String description, String username) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }
}
