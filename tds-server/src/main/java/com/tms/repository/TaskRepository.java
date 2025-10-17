package com.tms.repository;

import com.tms.entity.Task;
import com.tms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // 根据负责人查询任务
    List<Task> findByResponsiblePerson(User responsiblePerson);
    
    // 根据创建人查询任务
    List<Task> findByCreator(User creator);
    
    // 根据状态查询任务
    List<Task> findByStatus(String status);
    
    // 根据优先级查询任务
    List<Task> findByPriority(String priority);
    
    // 根据支付状态查询任务
    List<Task> findByPaymentStatus(String paymentStatus);
    
    // 根据任务名称模糊查询
    List<Task> findByNameContaining(String name);
    
    // 根据ID查询任务详情
    @Override
    Optional<Task> findById(Long id);
}