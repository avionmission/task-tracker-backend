package com.avionmission.tasks.services.impl;

import com.avionmission.tasks.domain.entities.TaskList;
import com.avionmission.tasks.domain.entities.User;
import com.avionmission.tasks.repositories.TaskListRepository;
import com.avionmission.tasks.services.TaskListService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskListServiceImpl implements TaskListService {

    private final TaskListRepository taskListRepository;

    public TaskListServiceImpl(TaskListRepository taskListRepository) {
        this.taskListRepository = taskListRepository;
    }

    @Override
    public List<TaskList> listTaskLists() {
        return taskListRepository.findAll();
    }

    @Override
    public List<TaskList> listTaskListsByUser(User user) {
        return taskListRepository.findByUser(user);
    }

    @Override
    public List<TaskList> listTaskListsByUserId(UUID userId) {
        return taskListRepository.findByUserId(userId);
    }

    @Override
    public TaskList createTaskList(TaskList taskList) {
        if(null != taskList.getId()) {
            throw new IllegalArgumentException("Task list already has ID!");
        }
        if(null == taskList.getTitle() || taskList.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task list title must be present!");
        }
        if(null == taskList.getUser()) {
            throw new IllegalArgumentException("Task list must have a user!");
        }
        LocalDateTime now = LocalDateTime.now();
        return taskListRepository.save(new TaskList(
                null,
                taskList.getTitle(),
                taskList.getDescription(),
                null,
                taskList.getUser(),
                now,
                now
        ));
    }

    @Override
    public Optional<TaskList> getTaskList(UUID id) {
        return taskListRepository.findById(id);
    }

    @Override
    public Optional<TaskList> getTaskListByUser(UUID id, User user) {
        return taskListRepository.findByIdAndUser(id, user);
    }

    @Override
    public Optional<TaskList> getTaskListByUserId(UUID id, UUID userId) {
        return taskListRepository.findByIdAndUserId(id, userId);
    }

    @Transactional
    @Override
    public TaskList updateTaskList(UUID taskListId, TaskList taskList) {
        if(null == taskList.getId()) {
            throw new IllegalArgumentException("Task list must have an ID");
        }

        if(!Objects.equals(taskList.getId(), taskListId)) {
            throw new IllegalArgumentException("Attempting to change task list ID, this is not permitted");
        }

        TaskList existingTaskList = taskListRepository.findById(taskListId).orElseThrow(() -> new IllegalArgumentException("Task list not found"));
        existingTaskList.setTitle(taskList.getTitle());
        existingTaskList.setDescription(taskList.getDescription());
        existingTaskList.setUpdated(LocalDateTime.now());
        return taskListRepository.save(existingTaskList);
    }

    @Transactional
    @Override
    public TaskList updateTaskListByUser(UUID taskListId, TaskList taskList, User user) {
        if(null == taskList.getId()) {
            throw new IllegalArgumentException("Task list must have an ID");
        }

        if(!Objects.equals(taskList.getId(), taskListId)) {
            throw new IllegalArgumentException("Attempting to change task list ID, this is not permitted");
        }

        TaskList existingTaskList = taskListRepository.findByIdAndUser(taskListId, user)
                .orElseThrow(() -> new IllegalArgumentException("Task list not found or access denied"));
        existingTaskList.setTitle(taskList.getTitle());
        existingTaskList.setDescription(taskList.getDescription());
        existingTaskList.setUpdated(LocalDateTime.now());
        return taskListRepository.save(existingTaskList);
    }

    @Override
    public void deleteTaskList(UUID taskListId) {
        taskListRepository.deleteById(taskListId);
    }

    @Override
    public void deleteTaskListByUser(UUID taskListId, User user) {
        TaskList taskList = taskListRepository.findByIdAndUser(taskListId, user)
                .orElseThrow(() -> new IllegalArgumentException("Task list not found or access denied"));
        taskListRepository.delete(taskList);
    }
}
