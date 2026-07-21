package com.example.devtask_backend.project;

import com.example.devtask_backend.common.ForbiddenException;
import com.example.devtask_backend.common.NotFoundException;
import com.example.devtask_backend.task.TaskRepository;
import com.example.devtask_backend.task.TaskStatus;
import com.example.devtask_backend.user.User;
import com.example.devtask_backend.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public ProjectService(
            ProjectRepository projectRepository,
            UserRepository userRepository,
            TaskRepository taskRepository
    ) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

//    완료율 계산 메서드
    private int calculateProgress(Long projectId) {
        long totalCount = taskRepository.countByProjectId(projectId);
        long doneCount = taskRepository.countByProjectIdAndStatus(projectId, TaskStatus.DONE);

        if (totalCount == 0) {
            return 0;
        }

        return (int) ((doneCount * 100) / totalCount);
    }

//    프로젝트 생성
    public ProjectResponse createProject(Long userId, ProjectRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("Project title은 필수입니다.");
        }

        String title = request.getTitle();
        String description = request.getDescription();

        Project project = new Project(title, description, user);

        Project savedProject = projectRepository.save(project);

        return new ProjectResponse(
                savedProject.getId(),
                savedProject.getTitle(),
                savedProject.getDescription(),
                savedProject.getUser().getUsername(),
                0
        );
    }

//    프로젝트 조회
    public List<ProjectResponse> getMyProjects(Long userId) {
        List<Project> projects = projectRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return projects.stream()
                .map(project -> {
                    int progress = calculateProgress(project.getId());

                    return new ProjectResponse(
                            project.getId(),
                            project.getTitle(),
                            project.getDescription(),
                            project.getUser().getUsername(),
                            progress
                    );
                })
                .toList();
    }

//    프로젝트 단건 조회
    public ProjectResponse getProject(Long userId, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("프로젝트를 찾을 수 없습니다."));

        if (!Objects.equals(project.getUser().getId(), userId)) {
            throw new ForbiddenException("권한이 없습니다.");
        }

        int progress = calculateProgress(project.getId());

        return new ProjectResponse(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getUser().getUsername(),
                progress
        );
    }

//    프로젝트 수정
    public ProjectResponse updateProject(Long userId, Long projectId, ProjectRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("프로젝트를 찾을 수 없습니다."));

        if (!Objects.equals(project.getUser().getId(), userId)) {
            throw new ForbiddenException("권한이 없습니다.");
        }

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("Project title은 필수입니다.");
        }

        String title = request.getTitle();
        String description = request.getDescription();

        project.update(title, description);

        Project projectSave = projectRepository.save(project);

        int progress = calculateProgress(project.getId());

        return new ProjectResponse(
                projectSave.getId(),
                projectSave.getTitle(),
                projectSave.getDescription(),
                projectSave.getUser().getUsername(),
                progress
        );
    }

//    프로젝트 삭제
    public void deleteProject(Long userId, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("프로젝트를 찾을 수 없습니다."));

        if (!Objects.equals(project.getUser().getId(), userId)) {
            throw new ForbiddenException("권한이 없습니다.");
        }

        projectRepository.delete(project);
    }
}
