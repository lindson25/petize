package com.projects.petize.repositories;

import com.projects.petize.entities.Subtask;
import com.projects.petize.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {
    List<Subtask> findByTaskId(Long taskId);

    List<Subtask> findByTaskIdAndStatusNot(Long taskId, TaskStatus status);
}
