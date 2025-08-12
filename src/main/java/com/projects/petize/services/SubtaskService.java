package com.projects.petize.services;

import com.projects.petize.dtos.SubtaskCreateDTO;
import com.projects.petize.dtos.SubtaskResponseDTO;
import com.projects.petize.dtos.SubtaskUpdateStatusDTO;
import com.projects.petize.entities.Subtask;
import com.projects.petize.entities.Task;
import com.projects.petize.entities.User;
import com.projects.petize.enums.TaskStatus;
import com.projects.petize.exceptions.AccessDeniedException;
import com.projects.petize.exceptions.SubtaskNotFoundException;
import com.projects.petize.exceptions.TaskNotFoundException;
import com.projects.petize.repositories.SubtaskRepository;
import com.projects.petize.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubtaskService {

    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;

    public SubtaskResponseDTO createSubtask(Long taskId, SubtaskCreateDTO dto) {
        Task task = getUserTask(taskId);

        Subtask subtask = new Subtask();
        subtask.setTitle(dto.title());
        subtask.setStatus(TaskStatus.PENDING);
        subtask.setTask(task);

        subtaskRepository.save(subtask);
        return toDTO(subtask);
    }

    public List<SubtaskResponseDTO> listByTask(Long taskId) {
        getUserTask(taskId);
        return subtaskRepository.findByTaskId(taskId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public SubtaskResponseDTO updateStatus(Long subtaskId, SubtaskUpdateStatusDTO dto) {
        Subtask subtask = getUserSubtask(subtaskId);
        subtask.setStatus(dto.status());
        subtaskRepository.save(subtask);
        return toDTO(subtask);
    }

    public void deleteSubtask(Long subtaskId) {
        Subtask subtask = getUserSubtask(subtaskId);
        subtaskRepository.delete(subtask);
    }

    private Task getUserTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);
        if (!task.getUserId().equals(getAuthenticatedUserId())) {
            throw new AccessDeniedException();
        }
        return task;
    }

    private Subtask getUserSubtask(Long subtaskId) {
        Subtask subtask = subtaskRepository.findById(subtaskId)
                .orElseThrow(SubtaskNotFoundException::new);
        if (!subtask.getTask().getUserId().equals(getAuthenticatedUserId())) {
            throw new AccessDeniedException();
        }
        return subtask;
    }

    private Long getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        if (principal instanceof User user) {
            return user.getId();
        } else {
            throw new AccessDeniedException();
        }
    }

    private SubtaskResponseDTO toDTO(Subtask subtask) {
        return new SubtaskResponseDTO(subtask.getId(), subtask.getTitle(), subtask.getStatus());
    }
}
