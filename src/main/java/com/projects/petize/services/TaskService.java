package com.projects.petize.services;

import com.projects.petize.dtos.TaskCreateDTO;
import com.projects.petize.dtos.TaskResponseDTO;
import com.projects.petize.dtos.TaskUpdateStatusDTO;
import com.projects.petize.entities.Task;
import com.projects.petize.entities.User;
import com.projects.petize.enums.TaskPriority;
import com.projects.petize.enums.TaskStatus;
import com.projects.petize.exceptions.AccessDeniedException;
import com.projects.petize.exceptions.HasPendingSubtasksException;
import com.projects.petize.exceptions.TaskNotFoundException;
import com.projects.petize.repositories.SubtaskRepository;
import com.projects.petize.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final SubtaskRepository subtaskRepository;

    public TaskResponseDTO createTask(TaskCreateDTO dto) {
        Task task = new Task();
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setDueDate(dto.dueDate());
        task.setStatus(dto.status());
        task.setPriority(dto.priority());
        task.setUserId(getAuthenticatedUserId());

        taskRepository.save(task);
        return toDTO(task);
    }

    public List<TaskResponseDTO> listTasks(TaskStatus status, TaskPriority priority, LocalDate dueDate) {
        Long userId = getAuthenticatedUserId();

        List<Task> tasks;
        if (status != null && priority != null && dueDate != null) {
            tasks = taskRepository.findByUserIdAndStatusAndPriorityAndDueDate(userId, status, priority, dueDate);
        } else {
            tasks = taskRepository.findByUserId(userId);
        }

        return tasks.stream().map(this::toDTO).toList();
    }

    public TaskResponseDTO updateStatus(Long id, TaskUpdateStatusDTO dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(TaskNotFoundException::new);

        if (!task.getUserId().equals(getAuthenticatedUserId())) {
            throw new AccessDeniedException();
        }

        if (dto.status() == TaskStatus.COMPLETED) {
            boolean hasPendingSubtasks = !subtaskRepository
                    .findByTaskIdAndStatusNot(id, TaskStatus.COMPLETED)
                    .isEmpty();
            if (hasPendingSubtasks) {
                throw new HasPendingSubtasksException();
            }
        }

        task.setStatus(dto.status());
        taskRepository.save(task);

        return toDTO(task);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(TaskNotFoundException::new);

        if (!task.getUserId().equals(getAuthenticatedUserId())) {
            throw new AccessDeniedException();
        }

        taskRepository.delete(task);
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

    private TaskResponseDTO toDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate().toString(),
                task.getStatus(),
                task.getPriority()
        );
    }
}
