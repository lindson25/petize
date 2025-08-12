package com.projects.petize.dtos;

import com.projects.petize.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record SubtaskUpdateStatusDTO(

        @NotNull(message = "Status is required")
        TaskStatus status
) {
}
