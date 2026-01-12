package com.avionmission.tasks.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Request to update an existing task list")
public record UpdateTaskListRequest(
        @Schema(description = "ID of the task list", required = true)
        UUID id,
        
        @Schema(description = "Title of the task list", example = "Updated Task List", required = true)
        String title,
        
        @Schema(description = "Description of the task list", example = "Updated description")
        String description
) {
}