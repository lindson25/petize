package com.projects.petize.dtos;

import com.projects.petize.enums.TaskPriority;
import com.projects.petize.enums.TaskStatus;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        String dueDate,
        TaskStatus status,
        TaskPriority priority
) {
}
