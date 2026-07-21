package com.example.devtask_backend.task;

import com.example.devtask_backend.auth.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
public class TaskController {
    private final TaskService taskService;
    private final AuthService authService;

    public TaskController(TaskService taskService,AuthService authService) {
        this.taskService = taskService;
        this.authService = authService;
    }

    @PostMapping
    public TaskResponse createTask(
            @RequestHeader ("Authorization") String authorization,
            @RequestBody TaskRequest request,
            @PathVariable Long projectId) {
        Long userId = authService.getUserIdFromAuthorization(authorization);

        return taskService.createTask(userId, projectId, request);
    }

//    Task List 조회
    @GetMapping
    public List<TaskResponse> getTasks(
            @RequestHeader ("Authorization") String authorization,
            @PathVariable Long projectId,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) TaskStatus status
    ) {
        Long userId = authService.getUserIdFromAuthorization(authorization);

        return taskService.getTasks(userId, projectId, status, priority);
    }

//    Task 단건 조회
    @GetMapping("/{taskId}")
    public TaskResponse getTask(
            @RequestHeader ("Authorization") String authorization,
            @PathVariable Long taskId,
            @PathVariable Long projectId
    ) {
        Long userId = authService.getUserIdFromAuthorization(authorization);

        return taskService.getTask(userId, projectId, taskId);
    }

//    Task 수정
    @PutMapping("/{taskId}")
    public TaskResponse updateTask(
            @RequestHeader ("Authorization") String authorization,
            @PathVariable Long taskId,
            @PathVariable Long projectId,
            @RequestBody TaskRequest request
    ) {
        Long userId = authService.getUserIdFromAuthorization(authorization);

        return taskService.updateTask(userId, projectId, taskId, request);
    }

//    Task 삭제
    @DeleteMapping("/{taskId}")
    public String deleteTask(
            @RequestHeader ("Authorization") String authorization,
            @PathVariable Long taskId,
            @PathVariable Long projectId
    ) {
        Long userId = authService.getUserIdFromAuthorization(authorization);

        taskService.deleteTask(userId, projectId, taskId);

        return "Task가 삭제되었습니다.";
    }
}
