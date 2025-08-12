package com.projects.petize.services;

import com.projects.petize.dtos.AttachmentResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AttachmentService {
    void uploadAttachment(Long taskId, MultipartFile file) throws IOException;

    Page<AttachmentResponseDTO> listAttachments(Long taskId, Pageable pageable);
}
