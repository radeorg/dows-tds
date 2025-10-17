package com.tms.repository;

import com.tms.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // 根据任务ID查询评论列表
    List<Comment> findByTaskIdOrderByCreatedAtDesc(Long taskId);
    
    // 根据用户ID查询评论列表
    List<Comment> findByUserId(Long userId);
}