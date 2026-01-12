package com.avionmission.tasks.services;

import com.avionmission.tasks.domain.entities.TaskList;
import com.avionmission.tasks.domain.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface TaskListService {
    List<TaskList> listTaskLists();
    List<TaskList> listTaskListsByUser(User user);
    List<TaskList> listTaskListsByUserId(UUID userId);
    TaskList createTaskList(TaskList taskList);
    Optional<TaskList> getTaskList(UUID id);
    Optional<TaskList> getTaskListByUser(UUID id, User user);
    Optional<TaskList> getTaskListByUserId(UUID id, UUID userId);
    TaskList updateTaskList(UUID taskListId, TaskList taskList);
    TaskList updateTaskListByUser(UUID taskListId, TaskList taskList, User user);
    void deleteTaskList(UUID taskListId);
    void deleteTaskListByUser(UUID taskListId, User user);
}
