package com.projects.petize.services;

import com.projects.petize.dtos.SubtaskCreateDTO;
import com.projects.petize.dtos.SubtaskResponseDTO;
import com.projects.petize.dtos.SubtaskUpdateStatusDTO;
import com.projects.petize.entities.Subtask;
import com.projects.petize.entities.Task;
import com.projects.petize.entities.User;
import com.projects.petize.enums.TaskStatus;
import com.projects.petize.exceptions.AccessDeniedException;
import com.projects.petize.repositories.SubtaskRepository;
import com.projects.petize.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubtaskServiceTest {

    @InjectMocks
    private SubtaskService subtaskService;

    @Mock
    private SubtaskRepository subtaskRepository;

    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private void mockAuthentication(Long userId) {
        Authentication auth = mock(Authentication.class);
        User user = new User();
        user.setId(userId);
        when(auth.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void createSubtask_shouldReturnDTO_whenSuccess() {
        mockAuthentication(1L);

        Task task = new Task();
        task.setUserId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        SubtaskCreateDTO dto = new SubtaskCreateDTO("Title");

        Subtask savedSubtask = new Subtask();
        savedSubtask.setId(10L);
        savedSubtask.setTitle(dto.title());
        savedSubtask.setStatus(TaskStatus.PENDING);
        savedSubtask.setTask(task);

        when(subtaskRepository.save(any(Subtask.class))).thenReturn(savedSubtask);

        var response = subtaskService.createSubtask(1L, dto);

        assertEquals("Title", response.title());
        assertEquals(TaskStatus.PENDING, response.status());
    }

    @Test
    void createSubtask_shouldThrowAccessDeniedException_whenUserMismatch() {
        mockAuthentication(2L);

        Task task = new Task();
        task.setUserId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        SubtaskCreateDTO dto = new SubtaskCreateDTO("Title");

        assertThrows(AccessDeniedException.class, () -> subtaskService.createSubtask(1L, dto));
    }

    @Test
    void listByTask_shouldReturnPagedDTOs() {
        mockAuthentication(1L);

        Subtask subtask = new Subtask();
        subtask.setId(1L);
        subtask.setTitle("Test");
        subtask.setStatus(TaskStatus.PENDING);

        Page<Subtask> page = new PageImpl<>(List.of(subtask));

        when(subtaskRepository.findByTaskId(eq(1L), any(Pageable.class))).thenReturn(page);

        Page<SubtaskResponseDTO> responsePage = subtaskService.listByTask(1L, 0, 10, "id", "asc");

        assertEquals(1, responsePage.getTotalElements());
        assertEquals("Test", responsePage.getContent().get(0).title());
    }

    @Test
    void updateStatus_shouldUpdateAndReturnDTO() {
        mockAuthentication(1L);

        Task task = new Task();
        task.setUserId(1L);

        Subtask subtask = new Subtask();
        subtask.setId(1L);
        subtask.setTask(task);
        subtask.setTitle("Test");
        subtask.setStatus(TaskStatus.PENDING);

        when(subtaskRepository.findById(1L)).thenReturn(Optional.of(subtask));

        SubtaskUpdateStatusDTO dto = new SubtaskUpdateStatusDTO(TaskStatus.COMPLETED);

        var response = subtaskService.updateStatus(1L, dto);

        assertEquals(TaskStatus.COMPLETED, response.status());
        verify(subtaskRepository, times(1)).save(subtask);
    }

    @Test
    void deleteSubtask_shouldDelete_whenAuthorized() {
        mockAuthentication(1L);

        Subtask subtask = new Subtask();
        subtask.setId(1L);
        Task task = new Task();
        task.setUserId(1L);
        subtask.setTask(task);

        when(subtaskRepository.findById(1L)).thenReturn(Optional.of(subtask));

        subtaskService.deleteSubtask(1L);

        verify(subtaskRepository, times(1)).delete(subtask);
    }
}
