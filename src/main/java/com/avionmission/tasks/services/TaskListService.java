package com.avionmission.tasks.services;

import com.avionmission.tasks.domain.entities.TaskList;

import java.util.List;


public interface TaskListService {
    List<TaskList> listTaskLists();
    TaskList createTaskList(TaskList taskList);
}
