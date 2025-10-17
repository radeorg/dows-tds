package com.tms.controller;

import com.tms.entity.Task;
import com.tms.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*") // 在生产环境中应该设置具体的前端域名
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // 创建任务
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    // 获取所有任务
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    // 根据ID获取任务
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 更新任务
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        try {
            Task updatedTask = taskService.updateTask(id, task);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 删除任务
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 根据负责人查询任务
    @GetMapping("/responsible/{userId}")
    public ResponseEntity<List<Task>> getTasksByResponsiblePerson(@PathVariable Long userId) {
        try {
            List<Task> tasks = taskService.getTasksByResponsiblePerson(userId);
            return ResponseEntity.ok(tasks);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 根据创建人查询任务
    @GetMapping("/creator/{userId}")
    public ResponseEntity<List<Task>> getTasksByCreator(@PathVariable Long userId) {
        try {
            List<Task> tasks = taskService.getTasksByCreator(userId);
            return ResponseEntity.ok(tasks);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 根据状态查询任务
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable String status) {
        List<Task> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }

    // 根据支付状态查询任务
    @GetMapping("/payment-status/{paymentStatus}")
    public ResponseEntity<List<Task>> getTasksByPaymentStatus(@PathVariable String paymentStatus) {
        List<Task> tasks = taskService.getTasksByPaymentStatus(paymentStatus);
        return ResponseEntity.ok(tasks);
    }

    // 搜索任务
    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasksByName(@RequestParam String name) {
        List<Task> tasks = taskService.searchTasksByName(name);
        return ResponseEntity.ok(tasks);
    }
}