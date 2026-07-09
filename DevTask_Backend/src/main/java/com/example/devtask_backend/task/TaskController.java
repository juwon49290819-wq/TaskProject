package com.example.devtask_backend.task;

import com.example.devtask_backend.auth.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
public class TaskController {
    private final JwtUtil jwtUtil;
    private final TaskService taskService;
    private final TaskRepository taskRepository;

    public TaskController(JwtUtil jwtUtil, TaskService taskService, TaskRepository taskRepository) {
        this.jwtUtil = jwtUtil;
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }

    @PostMapping
    public TaskResponse createTask(
            @RequestHeader ("Authorization") String authorization,
            @RequestBody TaskRequest request,
            @PathVariable Long projectId) {
        String token = authorization.replace("Bearer ", "");

        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        Long userId = jwtUtil.getUserId(token);

        return taskService.createTask(userId, projectId, request);
    }

//    Task List 조회
    @GetMapping
    public List<TaskResponse> getTasks(
            @RequestHeader ("Authorization") String authorization,
            @PathVariable Long projectId
    ) {
        String token = authorization.replace("Bearer ", "");

        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        Long userId = jwtUtil.getUserId(token);

        return taskService.getTasks(userId, projectId);
    }

//    Task 단건 조회
    @GetMapping("/{taskId}")
    public TaskResponse getTask(
            @RequestHeader ("Authorization") String authorization,
            @PathVariable Long taskId,
            @PathVariable Long projectId
    ) {
        String token = authorization.replace("Bearer ", "");

        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        Long userId = jwtUtil.getUserId(token);

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
        String token = authorization.replace("Bearer ", "");

        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        Long userId = jwtUtil.getUserId(token);

        return taskService.updateTask(userId, projectId, taskId, request);
    }

//    Task 삭제
    @DeleteMapping("/{taskId}")
    public String deleteTask(
            @RequestHeader ("Authorization") String authorization,
            @PathVariable Long taskId,
            @PathVariable Long projectId
    ) {
        String token = authorization.replace("Bearer ", "");

        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }

        Long userId = jwtUtil.getUserId(token);

        taskService.deleteTask(userId, projectId, taskId);

        return "Task가 삭제되었습니다.";
    }
}
