package com.projects.petize.controllers;

import com.projects.petize.dtos.SubtaskCreateDTO;
import com.projects.petize.dtos.SubtaskResponseDTO;
import com.projects.petize.dtos.SubtaskUpdateStatusDTO;
import com.projects.petize.services.SubtaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks/{taskId}/subtasks")
@RequiredArgsConstructor
public class SubtaskController {

    private final SubtaskService subtaskService;

    @PostMapping
    public ResponseEntity<SubtaskResponseDTO> create(
            @PathVariable Long taskId,
            @Valid @RequestBody SubtaskCreateDTO dto
    ) {
        return ResponseEntity.ok(subtaskService.createSubtask(taskId, dto));
    }

    @GetMapping
    public ResponseEntity<Page<SubtaskResponseDTO>> listByTask(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return ResponseEntity.ok(subtaskService.listByTask(taskId, page, size, sortBy, direction));
    }

    @PatchMapping("/{subtaskId}/status")
    public ResponseEntity<SubtaskResponseDTO> updateStatus(
            @PathVariable Long subtaskId,
            @Valid @RequestBody SubtaskUpdateStatusDTO dto
    ) {
        return ResponseEntity.ok(subtaskService.updateStatus(subtaskId, dto));
    }

    @DeleteMapping("/{subtaskId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long subtaskId
    ) {
        subtaskService.deleteSubtask(subtaskId);
        return ResponseEntity.noContent().build();
    }
}
