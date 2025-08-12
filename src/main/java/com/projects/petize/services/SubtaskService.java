package com.projects.petize.services;

import com.projects.petize.dtos.SubtaskCreateDTO;
import com.projects.petize.dtos.SubtaskResponseDTO;
import com.projects.petize.dtos.SubtaskUpdateStatusDTO;
import org.springframework.data.domain.Page;

public interface SubtaskService {
    SubtaskResponseDTO createSubtask(Long taskId, SubtaskCreateDTO dto);

    Page<SubtaskResponseDTO> listByTask(Long taskId, int page, int size, String sortBy, String direction);

    SubtaskResponseDTO updateStatus(Long subtaskId, SubtaskUpdateStatusDTO dto);

    void deleteSubtask(Long subtaskId);
}
