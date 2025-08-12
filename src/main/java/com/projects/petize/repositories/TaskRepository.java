package com.projects.petize.repositories;

import com.projects.petize.entities.Task;
import com.projects.petize.enums.TaskPriority;
import com.projects.petize.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserIdAndStatusAndPriorityAndDueDate(
            Long userId, TaskStatus status, TaskPriority priority, LocalDate dueDate
    );

    List<Task> findByUserId(Long userId);
}
