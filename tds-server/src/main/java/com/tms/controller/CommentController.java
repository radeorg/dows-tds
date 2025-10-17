package com.tms.controller;

import com.tms.entity.Comment;
import com.tms.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*") // 在生产环境中应该设置具体的前端域名
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 创建评论
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        try {
            Comment createdComment = commentService.createComment(comment);
            return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 删除评论
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 根据任务ID获取评论列表
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Comment>> getCommentsByTaskId(@PathVariable Long taskId) {
        try {
            List<Comment> comments = commentService.getCommentsByTaskId(taskId);
            return ResponseEntity.ok(comments);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 根据用户ID获取评论列表
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Comment>> getCommentsByUserId(@PathVariable Long userId) {
        try {
            List<Comment> comments = commentService.getCommentsByUserId(userId);
            return ResponseEntity.ok(comments);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}