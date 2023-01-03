package com.example.alba_pocket.controller;

import com.example.alba_pocket.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PostMapping("posts/{postId}/like")
    public ResponseEntity<?> postLike(@PathVariable Long postId){
        return likesService.postLike(postId);
    }

}
