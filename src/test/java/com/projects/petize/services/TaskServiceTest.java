package com.projects.petize.services;

import com.projects.petize.dtos.TaskCreateDTO;
import com.projects.petize.dtos.TaskResponseDTO;
import com.projects.petize.dtos.TaskUpdateStatusDTO;
import com.projects.petize.entities.Subtask;
import com.projects.petize.entities.Task;
import com.projects.petize.entities.User;
import com.projects.petize.enums.TaskPriority;
import com.projects.petize.enums.TaskStatus;
import com.projects.petize.exceptions.HasPendingSubtasksException;
import com.projects.petize.repositories.SubtaskRepository;
import com.projects.petize.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SubtaskRepository subtaskRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private void mockAuthentication() {
        Authentication auth = mock(Authentication.class);
        User user = new User();
        user.setId(1L);
        when(auth.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void createTask_shouldReturnDTO() {
        mockAuthentication();

        TaskCreateDTO dto = new TaskCreateDTO("Title", "Desc", LocalDate.now(), TaskStatus.PENDING, TaskPriority.MEDIUM);

        Task task = new Task();
        task.setId(1L);
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setDueDate(dto.dueDate());
        task.setStatus(dto.status());
        task.setPriority(dto.priority());
        task.setUserId(1L);

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDTO response = taskService.createTask(dto);

        assertEquals(dto.title(), response.title());
        assertEquals(dto.status(), response.status());
    }

    @Test
    void listTasks_shouldReturnPagedDTO() {
        mockAuthentication();

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Title");
        task.setStatus(TaskStatus.PENDING);
        task.setPriority(TaskPriority.HIGH);
        task.setDueDate(LocalDate.now());
        task.setUserId(1L);

        Page<Task> pageMock = new PageImpl<>(List.of(task));

        when(taskRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(pageMock);

        Page<TaskResponseDTO> response = taskService.listTasks(
                TaskStatus.PENDING,
                TaskPriority.HIGH,
                LocalDate.now(),
                0,
                10,
                "id",
                "asc"
        );

        assertEquals(1, response.getTotalElements());
        assertEquals("Title", response.getContent().getFirst().title());
    }

    @Test
    void updateStatus_shouldUpdate_whenNoPendingSubtasks() {
        mockAuthentication();

        Task task = new Task();
        task.setId(1L);
        task.setUserId(1L);
        task.setDueDate(LocalDate.now());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(subtaskRepository.findByTaskIdAndStatusNot(1L, TaskStatus.COMPLETED)).thenReturn(List.of());

        TaskUpdateStatusDTO dto = new TaskUpdateStatusDTO(TaskStatus.COMPLETED);

        TaskResponseDTO response = taskService.updateStatus(1L, dto);

        assertEquals(TaskStatus.COMPLETED, response.status());
        verify(taskRepository).save(task);
    }

    @Test
    void updateStatus_shouldThrowHasPendingSubtasksException_whenPendingSubtasks() {
        mockAuthentication();

        Task task = new Task();
        task.setId(1L);
        task.setUserId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(subtaskRepository.findByTaskIdAndStatusNot(1L, TaskStatus.COMPLETED))
                .thenReturn(List.of(new Subtask()));

        TaskUpdateStatusDTO dto = new TaskUpdateStatusDTO(TaskStatus.COMPLETED);

        assertThrows(HasPendingSubtasksException.class, () -> taskService.updateStatus(1L, dto));
    }

    @Test
    void deleteTask_shouldDelete_whenNoIncompleteSubtasks() {
        mockAuthentication();

        Task task = new Task();
        task.setUserId(1L);
        task.setSubtasks(List.of());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository).delete(task);
    }

    @Test
    void deleteTask_shouldThrowHasPendingSubtasksException_whenIncompleteSubtasks() {
        mockAuthentication();

        Task task = new Task();
        task.setUserId(1L);
        Subtask subtask = new Subtask();
        subtask.setStatus(TaskStatus.PENDING);
        task.setSubtasks(List.of(subtask));

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(HasPendingSubtasksException.class, () -> taskService.deleteTask(1L));
    }
}
