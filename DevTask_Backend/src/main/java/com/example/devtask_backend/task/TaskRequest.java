package com.example.devtask_backend.task;

import java.time.LocalDate;

public class TaskRequest {
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDate dueDate;

    public TaskRequest() {}

    public TaskRequest (
            String title,
            String description,
            TaskPriority priority,
            TaskStatus status,
            LocalDate dueDate
    ) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.dueDate = dueDate;
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

    public LocalDate getDueDate() {
        return dueDate;
    }
}
