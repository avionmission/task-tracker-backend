package com.avionmission.tasks.mappers.impl;

import com.avionmission.tasks.domain.dto.TaskListDto;
import com.avionmission.tasks.domain.entities.Task;
import com.avionmission.tasks.domain.entities.TaskList;
import com.avionmission.tasks.domain.entities.TaskStatus;
import com.avionmission.tasks.mappers.TaskListMapper;
import com.avionmission.tasks.mappers.TaskMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TaskListMapperImpl implements TaskListMapper {

    private final TaskMapper taskMapper;
    public TaskListMapperImpl(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskList fromDto(TaskListDto taskListDto) {
        TaskList taskList = new TaskList();
        taskList.setId(taskListDto.id());
        taskList.setTitle(taskListDto.title());
        taskList.setDescription(taskListDto.description());
        if (taskListDto.tasks() != null) {
            taskList.setTasks(taskListDto.tasks().stream()
                    .map(taskMapper::fromDto)
                    .toList());
        }
        return taskList;
    }

    @Override
    public TaskListDto toDto(TaskList taskList) {
        return new TaskListDto(
                taskList.getId(),
                taskList.getTitle(),
                taskList.getDescription(),
                Optional.ofNullable(taskList.getTasks())
                        .map(List::size)
                        .orElse(0),
                calculateTaskListProgress(taskList.getTasks()),
                Optional.ofNullable(taskList.getTasks())
                        .map(tasks ->
                                tasks.stream().map(taskMapper::toDto).toList())
                        .orElse(null)
        );
    }

    private Double calculateTaskListProgress(List<Task> tasks) {
        if(null == tasks){
            return null;
        }

        long closedTaskCount = tasks.stream().filter(task ->
            TaskStatus.CLOSED == task.getStatus()
        ).count();

        return (double) closedTaskCount / tasks.size();
    }
}
