package com.projects.petize.controllers;

import com.projects.petize.dtos.TaskCreateDTO;
import com.projects.petize.dtos.TaskResponseDTO;
import com.projects.petize.dtos.TaskUpdateStatusDTO;
import com.projects.petize.enums.TaskPriority;
import com.projects.petize.enums.TaskStatus;
import com.projects.petize.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(@Valid @RequestBody TaskCreateDTO dto) {
        return ResponseEntity.ok(taskService.createTask(dto));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> list(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) LocalDate dueDate
    ) {
        return ResponseEntity.ok(taskService.listTasks(status, priority, dueDate));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponseDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateStatusDTO dto
    ) {
        return ResponseEntity.ok(taskService.updateStatus(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
