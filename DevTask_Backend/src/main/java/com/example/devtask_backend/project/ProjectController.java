package com.example.devtask_backend.project;

import com.example.devtask_backend.auth.AuthService;
import com.example.devtask_backend.auth.JwtUtil;
import com.example.devtask_backend.common.UnauthorizedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final AuthService authService;

    public ProjectController(ProjectService projectService,AuthService authService) {
        this.projectService = projectService;
        this.authService = authService;
    }

//    프로젝트 생성
    @PostMapping
    public ProjectResponse createProject(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ProjectRequest request
    ) {
        Long userId = authService.getUserIdFromAuthorization(authorization);

        return projectService.createProject(userId, request);
    }

//    프로젝트 조회
    @GetMapping
    public List<ProjectResponse> getMyProjects(
            @RequestHeader("Authorization") String authorization
    ) {
        Long userId = authService.getUserIdFromAuthorization(authorization);

        return projectService.getMyProjects(userId);
    }

//    프로젝트 단건 조회
    @GetMapping("/{projectId}")
    public ProjectResponse getProject(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long projectId
    ) {
        Long userId = authService.getUserIdFromAuthorization(authorization);

        return projectService.getProject(userId, projectId);
    }

//    프로젝트 수정
    @PutMapping("/{projectId}")
    public ProjectResponse updateProject(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long projectId,
            @RequestBody ProjectRequest request
    ) {
        Long userId = authService.getUserIdFromAuthorization(authorization);

        return projectService.updateProject(userId, projectId, request);
    }

//    프로젝트 삭제
    @DeleteMapping("/{projectId}")
    public String deleteProject(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long projectId
    ) {
        Long userId = authService.getUserIdFromAuthorization(authorization);

        projectService.deleteProject(userId, projectId);

        return "프로젝트가 삭제되었습니다.";
    }
}
