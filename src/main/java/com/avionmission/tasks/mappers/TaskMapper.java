package com.avionmission.tasks.mappers;

import com.avionmission.tasks.domain.dto.TaskDto;
import com.avionmission.tasks.domain.entities.Task;

public interface TaskMapper {
    Task fromDto(TaskDto taskDto);
    TaskDto toDto(Task task);
}
