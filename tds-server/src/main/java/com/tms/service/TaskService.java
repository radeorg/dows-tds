package com.tms.service;

import com.tms.entity.Task;
import java.util.List;
import java.util.Optional;

public interface TaskService {
    
    // 创建任务
    Task createTask(Task task);
    
    // 更新任务
    Task updateTask(Long id, Task task);
    
    // 删除任务
    void deleteTask(Long id);
    
    // 根据ID查询任务
    Optional<Task> getTaskById(Long id);
    
    // 获取所有任务
    List<Task> getAllTasks();
    
    // 根据负责人查询任务
    List<Task> getTasksByResponsiblePerson(Long userId);
    
    // 根据创建人查询任务
    List<Task> getTasksByCreator(Long userId);
    
    // 根据状态查询任务
    List<Task> getTasksByStatus(String status);
    
    // 根据优先级查询任务
    List<Task> getTasksByPriority(String priority);
    
    // 根据支付状态查询任务
    List<Task> getTasksByPaymentStatus(String paymentStatus);
    
    // 根据名称搜索任务
    List<Task> searchTasksByName(String name);
}