package com.example.devtask_backend.project;

import com.example.devtask_backend.user.User;
import com.example.devtask_backend.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

//    프로젝트 생성
    public ProjectResponse createProject(Long userId, ProjectRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String title = request.getTitle();
        String description = request.getDescription();

        Project project = new Project(title, description, user);

        Project savedProject = projectRepository.save(project);

        return new ProjectResponse(
                savedProject.getId(),
                savedProject.getTitle(),
                savedProject.getDescription(),
                savedProject.getUser().getUsername());
    }

//    프로젝트 조회
    public List<ProjectResponse> getMyProjects(Long userId) {
        List<Project> projects = projectRepository.findByUserId(userId);
        return projects.stream()
                .map(project -> new ProjectResponse(
                        project.getId(),
                        project.getTitle(),
                        project.getDescription(),
                        project.getUser().getUsername()
                ))
                .toList();
    }

//    프로젝트 단건 조회
    public ProjectResponse getProject(Long userId, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트를 찾을 수 없습니다."));

        if (!Objects.equals(project.getUser().getId(), userId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        return new ProjectResponse(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getUser().getUsername());
    }
}
