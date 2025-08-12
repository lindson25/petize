package com.projects.petize.repositories;

import com.projects.petize.entities.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    Page<Attachment> findByTaskId(Long taskId, Pageable pageable);
}
