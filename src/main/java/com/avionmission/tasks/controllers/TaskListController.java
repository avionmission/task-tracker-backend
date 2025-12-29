package com.avionmission.tasks.controllers;

import com.avionmission.tasks.domain.dto.TaskListDto;
import com.avionmission.tasks.domain.entities.TaskList;
import com.avionmission.tasks.mappers.TaskListMapper;
import com.avionmission.tasks.services.TaskListService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/task-lists")
public class TaskListController {

    private final TaskListService taskListService;
    private final TaskListMapper taskListMapper;

    public TaskListController(TaskListService taskListService, TaskListMapper taskListMapper) {
        this.taskListMapper = taskListMapper;
        this.taskListService = taskListService;
    }

    @GetMapping
    public List<TaskListDto> listTaskLists() {
        return taskListService.listTaskLists()
                .stream()
                .map(taskListMapper::toDto)
                .toList();
    }

    @GetMapping("/test")
    public String test() {
        return "ALL OK!!";
    }

    @PostMapping
    public TaskListDto createTaskList(@RequestBody TaskListDto taskListDto) {
        TaskList createdTaskList = taskListService.createTaskList(
                taskListMapper.fromDto(taskListDto)
        );
        return  taskListMapper.toDto(createdTaskList);
    }
}
