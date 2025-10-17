package com.tms.service.impl;

import com.tms.entity.Task;
import com.tms.entity.User;
import com.tms.repository.TaskRepository;
import com.tms.repository.UserRepository;
import com.tms.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Task createTask(Task task) {
        task.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, Task updatedTask) {
        Optional<Task> existingTask = taskRepository.findById(id);
        if (existingTask.isPresent()) {
            Task task = existingTask.get();
            task.setName(updatedTask.getName());
            task.setDescription(updatedTask.getDescription());
            task.setResponsiblePerson(updatedTask.getResponsiblePerson());
            task.setEstimatedTime(updatedTask.getEstimatedTime());
            task.setStatus(updatedTask.getStatus());
            task.setPriority(updatedTask.getPriority());
            task.setReward(updatedTask.getReward());
            task.setPaymentStatus(updatedTask.getPaymentStatus());
            task.setDeadline(updatedTask.getDeadline());
            task.setUpdatedAt(LocalDateTime.now());
            return taskRepository.save(task);
        }
        throw new RuntimeException("Task not found with id: " + id);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getTasksByResponsiblePerson(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return taskRepository.findByResponsiblePerson(user.get());
        }
        throw new RuntimeException("User not found with id: " + userId);
    }

    @Override
    public List<Task> getTasksByCreator(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return taskRepository.findByCreator(user.get());
        }
        throw new RuntimeException("User not found with id: " + userId);
    }

    @Override
    public List<Task> getTasksByStatus(String status) {
        return taskRepository.findByStatus(status);
    }

    @Override
    public List<Task> getTasksByPriority(String priority) {
        return taskRepository.findByPriority(priority);
    }

    @Override
    public List<Task> getTasksByPaymentStatus(String paymentStatus) {
        return taskRepository.findByPaymentStatus(paymentStatus);
    }

    @Override
    public List<Task> searchTasksByName(String name) {
        return taskRepository.findByNameContaining(name);
    }
}