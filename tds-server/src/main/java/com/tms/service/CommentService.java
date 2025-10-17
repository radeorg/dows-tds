package com.tms.service;

import com.tms.entity.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentService {
    
    // 创建评论
    Comment createComment(Comment comment);
    
    // 删除评论
    void deleteComment(Long id);
    
    // 根据ID查询评论
    Optional<Comment> getCommentById(Long id);
    
    // 根据任务ID查询评论列表
    List<Comment> getCommentsByTaskId(Long taskId);
    
    // 根据用户ID查询评论列表
    List<Comment> getCommentsByUserId(Long userId);
}