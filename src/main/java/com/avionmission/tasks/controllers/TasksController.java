package com.avionmission.tasks.controllers;

import com.avionmission.tasks.domain.dto.TaskDto;
import com.avionmission.tasks.domain.entities.Task;
import com.avionmission.tasks.domain.entities.User;
import com.avionmission.tasks.mappers.TaskMapper;
import com.avionmission.tasks.services.TaskService;
import com.avionmission.tasks.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/task-lists/{task_list_id}/tasks")
public class TasksController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final UserService userService;

    public TasksController(TaskService taskService, TaskMapper taskMapper, UserService userService) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
        this.userService = userService;
    }

    @GetMapping
    public List<TaskDto> listTasks(@PathVariable("task_list_id") UUID taskListId, Authentication authentication) {
        User user = getCurrentUser(authentication);
        return taskService.listTasksByUser(taskListId, user)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @PostMapping
    public TaskDto createTask(
            @PathVariable("task_list_id") UUID taskListId,
            @RequestBody TaskDto taskDto,
            Authentication authentication
    ){
        User user = getCurrentUser(authentication);
        Task createdTask = taskService.createTaskByUser(taskListId, taskMapper.fromDto(taskDto), user);
        return taskMapper.toDto(createdTask);
    }

    @GetMapping(path = "/{task_id}")
    public Optional<TaskDto> getTask (
            @PathVariable("task_list_id") UUID taskListId,
            @PathVariable("task_id") UUID taskId,
            Authentication authentication
    ){
        User user = getCurrentUser(authentication);
        return taskService.getTaskByUser(taskListId, taskId, user).map(taskMapper::toDto);
    }

    @PutMapping(path = "/{task_id}")
    public TaskDto updateTask(
            @PathVariable("task_list_id") UUID taskListId,
            @PathVariable("task_id") UUID taskId,
            @RequestBody TaskDto taskDto,
            Authentication authentication
    ) {
        User user = getCurrentUser(authentication);
        Task updatedTask = taskService.updateTaskByUser(taskListId, taskId, taskMapper.fromDto(taskDto), user);
        return taskMapper.toDto(updatedTask);
    }

    @DeleteMapping(path = "/{task_id}")
    public void deleteTask(
            @PathVariable("task_list_id") UUID taskListId,
            @PathVariable("task_id") UUID taskId,
            Authentication authentication
    ) {
        User user = getCurrentUser(authentication);
        taskService.deleteTaskByUser(taskListId, taskId, user);
    }

    @GetMapping(path = "/all")
    public List<TaskDto> getAllUserTasks(Authentication authentication) {
        User user = getCurrentUser(authentication);
        return taskService.listAllTasksByUserId(user.getId())
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    private User getCurrentUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
