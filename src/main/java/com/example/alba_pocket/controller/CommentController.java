package com.example.alba_pocket.controller;

import com.example.alba_pocket.dto.CommentRequestDto;
import com.example.alba_pocket.dto.CommentResponseDto;
import com.example.alba_pocket.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    //댓글작성
    @PostMapping("/{postId}")
    public ResponseEntity<?> createComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto){

        return commentService.createComment(postId, requestDto);
    }
    //댓글조회
    @GetMapping("{postId}")
    public List<CommentResponseDto> getComments(@PathVariable Long postId){
        return commentService.getComments(postId);
    }
    //댓글수정
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto){
        return commentService.updateComment(commentId, requestDto);
    }
    //댓글삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId){
        return commentService.deleteComment(commentId);
    }

}
