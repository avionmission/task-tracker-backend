package com.avionmission.tasks.mappers;

import com.avionmission.tasks.domain.dto.TaskListDto;
import com.avionmission.tasks.domain.entities.TaskList;

public interface TaskListMapper {
    TaskList fromDto(TaskListDto taskListDto);
    TaskListDto toDto(TaskList taskList);
}
