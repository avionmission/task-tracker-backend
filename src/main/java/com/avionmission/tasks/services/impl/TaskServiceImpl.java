package com.avionmission.tasks.services.impl;

import com.avionmission.tasks.domain.entities.Task;
import com.avionmission.tasks.domain.entities.TaskList;
import com.avionmission.tasks.domain.entities.TaskPriority;
import com.avionmission.tasks.domain.entities.TaskStatus;
import com.avionmission.tasks.domain.entities.User;
import com.avionmission.tasks.repositories.TaskListRepository;
import com.avionmission.tasks.repositories.TaskRepository;
import com.avionmission.tasks.services.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskListRepository taskListRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
    }

    @Override
    public List<Task> listTasks(UUID taskListId) {
        return taskRepository.findByTaskListId(taskListId);
    }

    @Override
    public List<Task> listTasksByUser(UUID taskListId, User user) {
        return taskRepository.findByTaskListIdAndUserId(taskListId, user.getId());
    }

    @Override
    public List<Task> listTasksByUserId(UUID taskListId, UUID userId) {
        return taskRepository.findByTaskListIdAndUserId(taskListId, userId);
    }

    @Override
    public List<Task> listAllTasksByUserId(UUID userId) {
        return taskRepository.findByUserId(userId);
    }

    @Override
    public Task createTask(UUID taskListId, Task task) {
        if(null != task.getId())
            throw new IllegalArgumentException("Task already has an ID!");
        if(null == task.getTitle() || task.getTitle().isBlank())
            throw new IllegalArgumentException("Task must have a title!");
        TaskPriority taskPriority = Optional.ofNullable(task.getPriority()).orElse(TaskPriority.MEDIUM);
        TaskStatus taskStatus = TaskStatus.OPEN;

        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task list ID provided!"));
        LocalDateTime now = LocalDateTime.now();
        Task taskToSave = new Task(
                null,
                task.getTitle(),
                task.getDescription(),
                taskStatus,
                taskPriority,
                taskList,
                task.getDueDate(),
                now,
                now
        );

        return taskRepository.save(taskToSave);
    }

    @Override
    public Task createTaskByUser(UUID taskListId, Task task, User user) {
        if(null != task.getId())
            throw new IllegalArgumentException("Task already has an ID!");
        if(null == task.getTitle() || task.getTitle().isBlank())
            throw new IllegalArgumentException("Task must have a title!");
        TaskPriority taskPriority = Optional.ofNullable(task.getPriority()).orElse(TaskPriority.MEDIUM);
        TaskStatus taskStatus = TaskStatus.OPEN;

        TaskList taskList = taskListRepository.findByIdAndUser(taskListId, user)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task list ID or access denied!"));
        LocalDateTime now = LocalDateTime.now();
        Task taskToSave = new Task(
                null,
                task.getTitle(),
                task.getDescription(),
                taskStatus,
                taskPriority,
                taskList,
                task.getDueDate(),
                now,
                now
        );

        return taskRepository.save(taskToSave);
    }

    @Override
    public Optional<Task> getTask(UUID taskListId, UUID taskId) {
        return taskRepository.findByTaskListIdAndId(taskListId, taskId);
    }

    @Override
    public Optional<Task> getTaskByUser(UUID taskListId, UUID taskId, User user) {
        // First verify the task list belongs to the user, then get the task
        return taskListRepository.findByIdAndUser(taskListId, user)
                .flatMap(taskList -> taskRepository.findByTaskListIdAndId(taskListId, taskId));
    }

    @Override
    public Optional<Task> getTaskByUserId(UUID taskId, UUID userId) {
        return taskRepository.findByIdAndUserId(taskId, userId);
    }

    @Transactional
    @Override
    public Task updateTask(UUID taskListId, UUID taskId, Task task) {
        if(null == task.getId()){
            throw new IllegalArgumentException("Task must have an ID!");
        }
        if(!Objects.equals(taskId, task.getId())) {
            throw new IllegalArgumentException("Task ids do not match");
        }
        if(null == task.getPriority()) {
            throw new IllegalArgumentException("Task must have a valid priority");
        }
        if(null == task.getStatus()) {
            throw new IllegalArgumentException("Task must have a valid status");
        }
        Task existingTask = taskRepository.findByTaskListIdAndId(taskListId, taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found!"));
        
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setPriority(task.getPriority());
        existingTask.setStatus(task.getStatus());
        existingTask.setUpdated(LocalDateTime.now());

        return taskRepository.save(existingTask);
    }

    @Transactional
    @Override
    public Task updateTaskByUser(UUID taskListId, UUID taskId, Task task, User user) {
        if(null == task.getId()){
            throw new IllegalArgumentException("Task must have an ID!");
        }
        if(!Objects.equals(taskId, task.getId())) {
            throw new IllegalArgumentException("Task ids do not match");
        }
        if(null == task.getPriority()) {
            throw new IllegalArgumentException("Task must have a valid priority");
        }
        if(null == task.getStatus()) {
            throw new IllegalArgumentException("Task must have a valid status");
        }
        
        // Verify task list belongs to user first
        taskListRepository.findByIdAndUser(taskListId, user)
                .orElseThrow(() -> new IllegalArgumentException("Task list not found or access denied!"));
        
        Task existingTask = taskRepository.findByTaskListIdAndId(taskListId, taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found!"));
        
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setPriority(task.getPriority());
        existingTask.setStatus(task.getStatus());
        existingTask.setUpdated(LocalDateTime.now());

        return taskRepository.save(existingTask);
    }

    @Transactional
    @Override
    public void deleteTask(UUID taskListId, UUID taskId) {
        taskRepository.deleteByTaskListIdAndId(taskListId, taskId);
    }

    @Transactional
    @Override
    public void deleteTaskByUser(UUID taskListId, UUID taskId, User user) {
        // Verify task list belongs to user first
        taskListRepository.findByIdAndUser(taskListId, user)
                .orElseThrow(() -> new IllegalArgumentException("Task list not found or access denied!"));
        
        taskRepository.deleteByTaskListIdAndId(taskListId, taskId);
    }
}
