package com.projects.petize.services;

import com.projects.petize.entities.Attachment;
import com.projects.petize.entities.Task;
import com.projects.petize.entities.User;
import com.projects.petize.exceptions.AccessDeniedException;
import com.projects.petize.exceptions.TaskNotFoundException;
import com.projects.petize.repositories.AttachmentRepository;
import com.projects.petize.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttachmentServiceTest {

    @InjectMocks
    private AttachmentService attachmentService;

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        User user = new User();
        user.setId(1L);
        when(authentication.getPrincipal()).thenReturn(user);
    }

    @Test
    void uploadAttachment_shouldSaveAttachment_whenAuthorized() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());

        Task task = new Task();
        task.setUserId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(attachmentRepository.save(any(Attachment.class))).thenAnswer(i -> i.getArgument(0));

        attachmentService.uploadAttachment(1L, file);

        verify(attachmentRepository, times(1)).save(any(Attachment.class));
    }

    @Test
    void uploadAttachment_shouldThrowTaskNotFoundException_whenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());

        assertThrows(TaskNotFoundException.class, () -> attachmentService.uploadAttachment(1L, file));
    }

    @Test
    void uploadAttachment_shouldThrowAccessDeniedException_whenUserMismatch() {
        Task task = new Task();
        task.setUserId(2L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());

        assertThrows(AccessDeniedException.class, () -> attachmentService.uploadAttachment(1L, file));
    }
}
