package com.avionmission.tasks.services;

import com.avionmission.tasks.domain.entities.Task;
import com.avionmission.tasks.domain.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
    List<Task> listTasks(UUID taskListId);
    List<Task> listTasksByUser(UUID taskListId, User user);
    List<Task> listTasksByUserId(UUID taskListId, UUID userId);
    List<Task> listAllTasksByUserId(UUID userId);
    Task createTask(UUID taskListId, Task task);
    Task createTaskByUser(UUID taskListId, Task task, User user);
    Optional<Task> getTask(UUID taskListId, UUID taskId);
    Optional<Task> getTaskByUser(UUID taskListId, UUID taskId, User user);
    Optional<Task> getTaskByUserId(UUID taskId, UUID userId);
    Task updateTask(UUID taskListId, UUID taskId, Task task);
    Task updateTaskByUser(UUID taskListId, UUID taskId, Task task, User user);
    void deleteTask(UUID taskListId, UUID taskId);
    void deleteTaskByUser(UUID taskListId, UUID taskId, User user);
}
