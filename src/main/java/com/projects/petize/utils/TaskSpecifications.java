package com.projects.petize.utils;

import com.projects.petize.entities.Task;
import com.projects.petize.enums.TaskPriority;
import com.projects.petize.enums.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TaskSpecifications {

    public static Specification<Task> hasUserId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("userId"), userId);
    }

    public static Specification<Task> hasStatus(TaskStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Task> hasPriority(TaskPriority priority) {
        return (root, query, cb) -> cb.equal(root.get("priority"), priority);
    }

    public static Specification<Task> hasDueDate(LocalDate dueDate) {
        return (root, query, cb) -> cb.equal(root.get("dueDate"), dueDate);
    }
}
