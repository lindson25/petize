package com.projects.petize.dtos;

import com.projects.petize.enums.TaskStatus;

public record SubtaskResponseDTO(

        Long id,
        String title,
        TaskStatus status
) {
}
