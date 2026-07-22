package com.example.devtask_backend.task;
import com.example.devtask_backend.common.ForbiddenException;
import com.example.devtask_backend.common.NotFoundException;
import com.example.devtask_backend.project.Project;
import com.example.devtask_backend.project.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getProject().getId(),
                task.getDueDate()
        );
    }

    private void validateTaskAccess(Task task, Long userId, Long projectId) {
        if (!Objects.equals(task.getProject().getId(), projectId)) {
            throw new IllegalArgumentException("잘못된 프로젝트의 Task입니다.");
        }

        if (!Objects.equals(task.getProject().getUser().getId(), userId)) {
            throw new ForbiddenException("권한이 없습니다.");
        }
    }

    private Project getProjectAndValidateAccess(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("프로젝트를 찾을 수 없습니다."));

        if (!Objects.equals(project.getUser().getId(), userId)) {
            throw new ForbiddenException("권한이 없습니다.");
        }

        return project;
    }

//    Task 생성 메서드
    public TaskResponse createTask(Long userId, Long projectId, TaskRequest request) {

        Project project = getProjectAndValidateAccess(projectId, userId);

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task title은 필수입니다.");
        }

        String title = request.getTitle();
        String description = request.getDescription();
        TaskPriority priority = request.getPriority();
        LocalDate dueDate = request.getDueDate();

        Task task = new Task(title, description, priority, project, dueDate);

        Task savedTask = taskRepository.save(task);

        return toResponse(savedTask);
    }

//    Task 조회 메서드
    public List<TaskResponse> getTasks(
            Long userId,
            Long projectId,
            TaskStatus status,
            TaskPriority priority,
            String keyword
    ) {
        getProjectAndValidateAccess(projectId, userId);

        List<Task> tasks = taskRepository.searchTasks(projectId, keyword, status, priority);

        return tasks.stream()
                .map(this::toResponse)
                .toList();
    }

//    오늘까지 Task 조회 메서드
    public List<TaskResponse> getTodayTasks(
            Long userId,
            Long projectId
    ) {
        getProjectAndValidateAccess(projectId, userId);

        LocalDate today = LocalDate.now();

        List<Task> tasks =
                taskRepository.findByProjectIdAndDueDateLessThanEqualAndStatusNotOrderByDueDateAsc(
                        projectId,
                        today,
                        TaskStatus.DONE
                );

        return tasks.stream()
                .map(this::toResponse)
                .toList();
    }

//    Task 단건 조회 메서드
    public TaskResponse getTask(Long userId, Long projectId, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task를 찾을 수 없습니다."));

        validateTaskAccess(task, userId, projectId);

        return toResponse(task);
    }

//    Task 수정 메서드
    public TaskResponse updateTask(Long userId, Long projectId, Long taskId, TaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task를 찾을 수 없습니다."));

        validateTaskAccess(task, userId, projectId);

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task title은 필수입니다.");
        }

        String title = request.getTitle();
        String description = request.getDescription();
        TaskPriority priority = request.getPriority();
        TaskStatus status = request.getStatus();
        LocalDate dueDate = request.getDueDate();

        task.update(title, description, status, priority, dueDate);

        Task savedTask = taskRepository.save(task);

        return toResponse(savedTask);
    }

//    Task 완료 처리 메서드
    public TaskResponse done(Long userId,Long projectId, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task를 찾을 수 없습니다."));

        validateTaskAccess(task, userId, projectId);

        task.done();

        Task savedTask = taskRepository.save(task);

        return toResponse(savedTask);
    }

    //    Task 다시 열기 메서드
    public TaskResponse reopen(Long userId,Long projectId, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task를 찾을 수 없습니다."));

        validateTaskAccess(task, userId, projectId);

        task.reopen();

        Task savedTask = taskRepository.save(task);

        return toResponse(savedTask);
    }

//    Task 삭제 메서드
    public void deleteTask(Long userId, Long projectId, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task를 찾을 수 없습니다."));

        validateTaskAccess(task, userId, projectId);

        taskRepository.delete(task);
    }
}
