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
        AND (:status IS NULL OR t.status = :status)
        AND (:priority IS NULL OR t.priority = :priority)   
        AND (
            :keyword IS NULL
            OR :keyword = ''
            OR t.title LIKE concat('%', :keyword, '%')
            OR t.description LIKE concat('%', :keyword, '%')
            )     
    ORDER BY t.createdAt DESC
    """)
        List<Task> searchTasks(
                @Param("projectId") Long projectId,
                @Param("keyword") String keyword,
                @Param("status") TaskStatus status,
                @Param("priority") TaskPriority priority
    );
        long countByProjectId(Long projectId);
        long countByProjectIdAndStatus(Long projectId, TaskStatus status);
}
