package com.avionmission.tasks.controllers;

import com.avionmission.tasks.domain.dto.CreateTaskListRequest;
import com.avionmission.tasks.domain.dto.TaskListDto;
import com.avionmission.tasks.domain.dto.UpdateTaskListRequest;
import com.avionmission.tasks.domain.entities.TaskList;
import com.avionmission.tasks.domain.entities.User;
import com.avionmission.tasks.mappers.TaskListMapper;
import com.avionmission.tasks.services.TaskListService;
import com.avionmission.tasks.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/task-lists")
@Tag(name = "Task Lists")
@SecurityRequirement(name = "Bearer Authentication")
public class TaskListController {

    private final TaskListService taskListService;
    private final TaskListMapper taskListMapper;
    private final UserService userService;

    public TaskListController(TaskListService taskListService, TaskListMapper taskListMapper, UserService userService) {
        this.taskListMapper = taskListMapper;
        this.taskListService = taskListService;
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all task lists")
    public List<TaskListDto> listTaskLists(Authentication authentication) {
        User user = getCurrentUser(authentication);
        return taskListService.listTaskListsByUser(user)
                .stream()
                .map(taskListMapper::toDto)
                .toList();
    }

    @PostMapping
    @Operation(summary = "Create a new task list")
    public TaskListDto createTaskList(@RequestBody CreateTaskListRequest request, Authentication authentication) {
        User user = getCurrentUser(authentication);
        
        TaskList taskList = new TaskList();
        taskList.setTitle(request.title());
        taskList.setDescription(request.description());
        taskList.setUser(user);
        
        TaskList createdTaskList = taskListService.createTaskList(taskList);
        return taskListMapper.toDto(createdTaskList);
    }

    @GetMapping("/{task_list_id}")
    @Operation(summary = "Get task list by ID")
    public Optional<TaskListDto> getTaskList(@PathVariable("task_list_id") UUID taskListId, Authentication authentication) {
        User user = getCurrentUser(authentication);
        return taskListService.getTaskListByUser(taskListId, user).map(taskListMapper::toDto);
    }

    @PutMapping("/{task_list_id}")
    @Operation(summary = "Update task list")
    public TaskListDto updateTaskList(
            @PathVariable("task_list_id") UUID taskListId,
            @RequestBody UpdateTaskListRequest request,
            Authentication authentication
    ) {
        User user = getCurrentUser(authentication);
        
        TaskList taskList = new TaskList();
        taskList.setId(request.id());
        taskList.setTitle(request.title());
        taskList.setDescription(request.description());
        
        TaskList updatedTaskList = taskListService.updateTaskListByUser(taskListId, taskList, user);
        return taskListMapper.toDto(updatedTaskList);
    }

    @DeleteMapping("/{task_list_id}")
    @Operation(summary = "Delete task list")
    public void deleteTaskList(@PathVariable("task_list_id") UUID taskListId, Authentication authentication) {
        User user = getCurrentUser(authentication);
        taskListService.deleteTaskListByUser(taskListId, user);
    }

    private User getCurrentUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
