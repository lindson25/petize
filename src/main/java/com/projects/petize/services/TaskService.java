package com.projects.petize.services;

import com.projects.petize.dtos.TaskCreateDTO;
import com.projects.petize.dtos.TaskResponseDTO;
import com.projects.petize.dtos.TaskUpdateStatusDTO;
import com.projects.petize.enums.TaskPriority;
import com.projects.petize.enums.TaskStatus;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface TaskService {
    TaskResponseDTO createTask(TaskCreateDTO dto);

    Page<TaskResponseDTO> listTasks(TaskStatus status, TaskPriority priority, LocalDate dueDate,
                                    int page, int size, String sortBy, String direction);

    TaskResponseDTO updateStatus(Long id, TaskUpdateStatusDTO dto);

    void deleteTask(Long id);
}
