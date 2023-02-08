package com.example.alba_pocket.controller;

import com.example.alba_pocket.dto.MyPageRequestDto;
import com.example.alba_pocket.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/mypage")
    public ResponseEntity<?> getMypage(@RequestParam int page, @RequestParam int size) {
        return myPageService.getMypage(page-1, size);
    }

    @PutMapping("/mypage")
    public ResponseEntity<?> updateMypage(@ModelAttribute MyPageRequestDto.MyPageAttributeRequestDto mypageAttributeRequestDto) throws IOException{
        return myPageService.updateMypage(mypageAttributeRequestDto);

    }
    @GetMapping("/mypage/like")
    public ResponseEntity<?> likeMypage(@RequestParam int page, @RequestParam int size) {
        return myPageService.likeMypage( page-1, size);
    }


    @DeleteMapping("/mypage/comments")
    public ResponseEntity<?> deleteMypageComments(@RequestBody MyPageRequestDto.MyPageDeleteRequestDto mypageDeleteRequestDto) {
        return myPageService.deleteMyPageComments(mypageDeleteRequestDto);
    }


    @GetMapping("/mypage/comments")
    public ResponseEntity<?> commentMypage(@RequestParam int page, @RequestParam int size) {
        return myPageService.commentMypage(page-1, size);
    }

}
