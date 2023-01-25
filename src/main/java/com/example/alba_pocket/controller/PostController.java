package com.example.alba_pocket.controller;

import com.example.alba_pocket.dto.PostRequestDto;
import com.example.alba_pocket.model.PostSearchKeyword;
import com.example.alba_pocket.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @PostMapping
    public ResponseEntity<?> createPost(@RequestPart(value = "file") MultipartFile file,
                                        @RequestPart(value = "data") PostRequestDto requestDto) throws IOException {
        return postService.createPost(file, requestDto);
    }

//    @GetMapping
//    public ResponseEntity<?> getPosts(){ return postService.getPosts(); }
    @GetMapping
    public ResponseEntity<?> getPosts(@RequestParam int page, @RequestParam int size) {
        return postService.getPosts(page-1, size); }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable Long postId){
        return postService.getPost(postId);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto requestDto){
        return postService.updatePost(postId, requestDto);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId){
        return postService.deletePost(postId);
    }

    @GetMapping("/category")
    public ResponseEntity<?> categoryGetPosts(@RequestParam int page, @RequestParam int size, @RequestParam String category){
        return postService.categoryGetPosts(page-1, size, category);
    }


    //게시글 검색
    @GetMapping("/search")
    public ResponseEntity<?> searchPost(@RequestParam PostSearchKeyword keyword, @RequestParam int page, @RequestParam int size) {
        return postService.searchPost(keyword, page-1, size);
    }
}
