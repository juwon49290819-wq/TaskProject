package com.example.devtask_backend.task;
import com.example.devtask_backend.common.ForbiddenException;
import com.example.devtask_backend.common.NotFoundException;
import com.example.devtask_backend.project.Project;
import com.example.devtask_backend.project.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

//    Task 생성 메서드
    public TaskResponse createTask(Long userId, Long projectId, TaskRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("프로젝트를 찾을 수 없습니다."));

        if (!Objects.equals(project.getUser().getId(), userId)) {
            throw new ForbiddenException("권한이 없습니다.");
        }

        String title = request.getTitle();
        String description = request.getDescription();
        TaskPriority priority = request.getPriority();

        Task task = new Task(title, description, priority, project);

        Task savedTask = taskRepository.save(task);

        return new TaskResponse(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.getStatus(),
                savedTask.getPriority(),
                savedTask.getProject().getId()
        );
    }

//    Task 조회 메서드
    public List<TaskResponse> getTasks(Long userId, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("프로젝트를 찾을 수 없습니다."));

        if (!Objects.equals(project.getUser().getId(), userId)) {
            throw new ForbiddenException("권한이 없습니다.");
        }

        List<Task> tasks = taskRepository.findByProjectId(projectId);

        return tasks.stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getPriority(),
                        task.getProject().getId()
                ))
                .toList();
    }

//    Task 단건 조회 메서드
    public TaskResponse getTask(Long userId, Long projectId, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task를 찾을 수 없습니다."));

        if (!Objects.equals(task.getProject().getId(), projectId)) {
            throw new IllegalArgumentException("잘못된 프로젝트의 Task입니다.");
        }

        if (!Objects.equals(task.getProject().getUser().getId(), userId)) {
            throw new ForbiddenException("권한이 없습니다.");
        }

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getProject().getId()
        );
    }

//    Task 수정 메서드
    public TaskResponse updateTask(Long userId, Long projectId, Long taskId, TaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task를 찾을 수 없습니다."));

        if (!Objects.equals(task.getProject().getId(), projectId)) {
            throw new IllegalArgumentException("잘못된 프로젝트의 Task입니다.");
        }

        if (!Objects.equals(task.getProject().getUser().getId(), userId)) {
            throw new ForbiddenException("권한이 없습니다.");
        }

        String title = request.getTitle();
        String description = request.getDescription();
        TaskPriority priority = request.getPriority();
        TaskStatus status = request.getStatus();

        task.update(title, description, status, priority);

        Task savedTask = taskRepository.save(task);

        return new TaskResponse(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.getStatus(),
                savedTask.getPriority(),
                savedTask.getProject().getId()
        );
    }

//    Task 삭제 메서드
    public void deleteTask(Long userId, Long projectId, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task를 찾을 수 없습니다."));

        if (!Objects.equals(task.getProject().getId(), projectId)) {
            throw new IllegalArgumentException("잘못된 프로젝트의 Task입니다.");
        }

        if (!Objects.equals(task.getProject().getUser().getId(), userId)) {
            throw new ForbiddenException("권한이 없습니다.");
        }

        taskRepository.delete(task);
    }
}
