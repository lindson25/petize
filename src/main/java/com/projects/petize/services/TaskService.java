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
import com.projects.petize.utils.TaskSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

    public Page<TaskResponseDTO> listTasks(TaskStatus status, TaskPriority priority, LocalDate dueDate,
                                           int page, int size, String sortBy, String direction) {
        Long userId = getAuthenticatedUserId();

        Pageable pageable = PageRequest.of(page, size,
                direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );

        Specification<Task> spec = TaskSpecifications.hasUserId(userId);

        if (status != null) spec = spec.and(TaskSpecifications.hasStatus(status));
        if (priority != null) spec = spec.and(TaskSpecifications.hasPriority(priority));
        if (dueDate != null) spec = spec.and(TaskSpecifications.hasDueDate(dueDate));

        Page<Task> tasks = taskRepository.findAll(spec, pageable);
        return tasks.map(this::toDTO);
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

        boolean hasIncompleteSubtasks = task.getSubtasks().stream()
                .anyMatch(subtask -> subtask.getStatus() != TaskStatus.COMPLETED);

        if (hasIncompleteSubtasks) {
            throw new HasPendingSubtasksException();
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
