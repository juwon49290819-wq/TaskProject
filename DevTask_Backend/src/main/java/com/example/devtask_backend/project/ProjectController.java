package com.example.devtask_backend.project;

import com.example.devtask_backend.auth.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final JwtUtil jwtUtil;

    public ProjectController(ProjectService projectService, JwtUtil jwtUtil) {
        this.projectService = projectService;
        this.jwtUtil = jwtUtil;
    }

//    프로젝트 생성
    @PostMapping
    public ProjectResponse createProject(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody ProjectRequest request
    ) {
        String token = authorizationHeader.replace("Bearer ", "");

        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        Long userId = jwtUtil.getUserId(token);

        return projectService.createProject(userId, request);
    }

//    프로젝트 조회
    @GetMapping
    public List<ProjectResponse> getMyProjects(
            @RequestHeader("Authorization") String authorization
    ) {
        String token = authorization.replace("Bearer ", "");

        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        Long userId = jwtUtil.getUserId(token);

        return projectService.getMyProjects(userId);
    }

//    프로젝트 단건 조회
    @GetMapping("/{projectId}")
    public ProjectResponse getProject(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long projectId
    ) {
        String token = authorization.replace("Bearer ", "");

        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        Long userId = jwtUtil.getUserId(token);

        return projectService.getProject(userId, projectId);
    }

//    프로젝트 수정
    @PutMapping("/{projectId}")
    public ProjectResponse updateProject(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long projectId,
            @RequestBody ProjectRequest request
    ) {
        String token = authorization.replace("Bearer ", "");

        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        Long userId = jwtUtil.getUserId(token);

        return projectService.updateProject(userId, projectId, request);
    }

//    프로젝트 삭제
    @DeleteMapping("/{projectId}")
    public String deleteProject(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long projectId
    ) {
        String token = authorization.replace("Bearer ", "");

        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }

        Long userId = jwtUtil.getUserId(token);

        projectService.deleteProject(userId, projectId);

        return "프로젝트가 삭제되었습니다.";
    }
}
