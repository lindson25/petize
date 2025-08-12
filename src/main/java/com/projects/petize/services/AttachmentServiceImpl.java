package com.projects.petize.services;

import com.projects.petize.dtos.AttachmentResponseDTO;
import com.projects.petize.entities.Attachment;
import com.projects.petize.entities.Task;
import com.projects.petize.entities.User;
import com.projects.petize.exceptions.AccessDeniedException;
import com.projects.petize.exceptions.TaskNotFoundException;
import com.projects.petize.repositories.AttachmentRepository;
import com.projects.petize.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;

    @Override
    public void uploadAttachment(Long taskId, MultipartFile file) throws IOException {
        Long userId = getAuthenticatedUserId();

        Task task = taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);

        if (!task.getUserId().equals(userId)) {
            throw new AccessDeniedException();
        }

        String uploadDir = "uploads/";
        Files.createDirectories(Paths.get(uploadDir));

        String filePath = uploadDir + UUID.randomUUID() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        Attachment attachment = new Attachment();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFileType(file.getContentType());
        attachment.setFileUrl(filePath);
        attachment.setTask(task);

        attachmentRepository.save(attachment);
    }

    @Override
    public Page<AttachmentResponseDTO> listAttachments(Long taskId, Pageable pageable) {
        Long userId = getAuthenticatedUserId();

        Task task = taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);

        if (!task.getUserId().equals(userId)) {
            throw new AccessDeniedException();
        }

        return attachmentRepository.findByTaskId(taskId, pageable)
                .map(this::toDTO);
    }

    private AttachmentResponseDTO toDTO(Attachment att) {
        return new AttachmentResponseDTO(
                att.getId(),
                att.getFileName(),
                att.getFileType(),
                att.getFileUrl()
        );
    }

    private Long getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        if (principal instanceof User user) {
            return user.getId();
        } else {
            throw new AccessDeniedException();
        }
    }
}
