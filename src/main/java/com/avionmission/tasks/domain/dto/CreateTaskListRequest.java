package com.avionmission.tasks.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to create a new task list")
public record CreateTaskListRequest(
        @Schema(description = "Title of the task list", example = "My Task List", required = true)
        String title,
        
        @Schema(description = "Description of the task list", example = "This is my task list description")
        String description
) {
}