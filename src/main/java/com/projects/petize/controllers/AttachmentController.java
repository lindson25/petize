package com.projects.petize.controllers;

import com.projects.petize.dtos.AttachmentResponseDTO;
import com.projects.petize.services.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/tasks/{taskId}/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(
            @PathVariable Long taskId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        attachmentService.uploadAttachment(taskId, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Page<AttachmentResponseDTO>> list(
            @PathVariable Long taskId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(attachmentService.listAttachments(taskId, pageable));
    }
}
