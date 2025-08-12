package com.projects.petize.dtos;

public record AttachmentResponseDTO(
        Long id,
        String fileName,
        String fileType,
        String fileUrl
) {
}
