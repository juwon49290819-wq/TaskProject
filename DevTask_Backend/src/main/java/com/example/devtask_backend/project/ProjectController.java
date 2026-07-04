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

//    프로젝트 생성 요청
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

//    프로젝트 조회 요청
    @GetMapping
    public List<ProjectResponse> getMyProject(
            @RequestHeader("Authorization") String authorization
    ) {
        String token = authorization.replace("Bearer ","");

        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        Long userId = jwtUtil.getUserId(token);

        return projectService.getMyProjects(userId);
    }
}
