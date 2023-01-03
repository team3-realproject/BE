package com.example.alba_pocket.controller;

import com.example.alba_pocket.dto.CommentRequestDto;
import com.example.alba_pocket.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<?> createComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto){

        return commentService.createComment(postId, requestDto);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto){
        return commentService.updateComment(commentId, requestDto);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId){
        return commentService.deleteComment(commentId);
    }

}
