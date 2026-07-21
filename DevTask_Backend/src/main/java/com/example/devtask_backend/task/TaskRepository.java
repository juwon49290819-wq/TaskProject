package com.example.devtask_backend.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("""
    SELECT t
    FROM Task t
    WHERE t.project.id = :projectId
      AND (
        t.title LIKE %:keyword%
        OR t.description LIKE %:keyword%
      )
    ORDER BY t.createdAt DESC
""")
    List<Task> searchByKeyword(
            @Param("projectId") Long projectId,
            @Param("keyword") String keyword
    );
    List<Task> findByProjectIdOrderByCreatedAtDesc(Long projectId);
    List<Task> findByProjectIdAndStatusOrderByCreatedAtDesc(Long projectId, TaskStatus status);
    List<Task> findByProjectIdAndPriorityOrderByCreatedAtDesc(Long projectId, TaskPriority priority);
    List<Task> findByProjectIdAndStatusAndPriorityOrderByCreatedAtDesc(
            Long projectId,
            TaskStatus status,
            TaskPriority priority
    );
}
