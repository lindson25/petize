package com.projects.petize.dtos;

import jakarta.validation.constraints.NotBlank;

public record SubtaskCreateDTO(

        @NotBlank(message = "Title is required")
        String title
) {
}
