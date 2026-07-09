package com.example.devtask_backend.task;

public class TaskRequest {
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;

    public TaskRequest() {}

    public TaskRequest (String title, String description, TaskPriority priority, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public TaskStatus getStatus() {
        return status;
    }
}
