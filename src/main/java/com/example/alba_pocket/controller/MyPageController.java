package com.example.alba_pocket.controller;

import com.example.alba_pocket.dto.MypageAttributeRequestDto;
import com.example.alba_pocket.dto.MypageReqeustDto;
import com.example.alba_pocket.entity.Post;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/mypage")
    public ResponseEntity<?> getMypage() {
        return myPageService.getMypage();
    }

//    @PutMapping(value = "/mypage", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<?> updateMypage(@RequestPart(value = "file") MultipartFile file,
//                                          @RequestPart MypageReqeustDto data) throws IOException {
//    return myPageService.updateMypage(file, data);

    @PutMapping("/mypage")
    public ResponseEntity<?> updateMypage(@ModelAttribute MypageAttributeRequestDto mypageAttributeRequestDto) throws IOException{
        return myPageService.updateMypage(mypageAttributeRequestDto);

    }

}
