package com.projects.petize.dtos;

import com.projects.petize.enums.TaskPriority;
import com.projects.petize.enums.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskCreateDTO(

        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Due date is required")
        @FutureOrPresent(message = "The due date cannot be in the past")
        LocalDate dueDate,

        @NotNull(message = "Status is required")
        TaskStatus status,

        @NotNull(message = "Priority is required")
        TaskPriority priority
) {
}
