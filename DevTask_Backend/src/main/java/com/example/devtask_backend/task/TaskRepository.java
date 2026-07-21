package com.example.devtask_backend.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectIdOrderByCreatedAtDesc(Long projectId);
    List<Task> findByProjectIdAndStatusOrderByCreatedAtDesc(Long projectId, TaskStatus status);
    List<Task> findByProjectIdAndPriorityOrderByCreatedAtDesc(Long projectId, TaskPriority priority);
    List<Task> findByProjectIdAndStatusAndPriorityOrderByCreatedAtDesc(
            Long projectId,
            TaskStatus status,
            TaskPriority priority
    );
}
