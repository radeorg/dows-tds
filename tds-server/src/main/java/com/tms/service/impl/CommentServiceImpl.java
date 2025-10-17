package com.tms.service.impl;

import com.tms.entity.Comment;
import com.tms.repository.CommentRepository;
import com.tms.repository.TaskRepository;
import com.tms.repository.UserRepository;
import com.tms.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, 
                           TaskRepository taskRepository,
                           UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Comment createComment(Comment comment) {
        // 验证任务和用户是否存在
        if (!taskRepository.existsById(comment.getTask().getId())) {
            throw new RuntimeException("Task not found with id: " + comment.getTask().getId());
        }
        if (!userRepository.existsById(comment.getUser().getId())) {
            throw new RuntimeException("User not found with id: " + comment.getUser().getId());
        }
        
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Comment not found with id: " + id);
        }
        commentRepository.deleteById(id);
    }

    @Override
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<Comment> getCommentsByTaskId(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found with id: " + taskId);
        }
        return commentRepository.findByTaskIdOrderByCreatedAtDesc(taskId);
    }

    @Override
    public List<Comment> getCommentsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return commentRepository.findByUserId(userId);
    }
}