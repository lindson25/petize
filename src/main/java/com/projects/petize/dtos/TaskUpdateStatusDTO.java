package com.projects.petize.dtos;

import com.projects.petize.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record TaskUpdateStatusDTO(
        @NotNull(message = "O status é obrigatório")
        TaskStatus status
) {
}
