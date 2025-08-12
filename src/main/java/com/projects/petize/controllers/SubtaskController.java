package com.projects.petize.controllers;

import com.projects.petize.dtos.SubtaskCreateDTO;
import com.projects.petize.dtos.SubtaskResponseDTO;
import com.projects.petize.dtos.SubtaskUpdateStatusDTO;
import com.projects.petize.services.SubtaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<SubtaskResponseDTO>> list(@PathVariable Long taskId) {
        return ResponseEntity.ok(subtaskService.listByTask(taskId));
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
