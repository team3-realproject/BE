package com.example.alba_pocket.controller;

import com.example.alba_pocket.dto.LoginRequestDto;
import com.example.alba_pocket.dto.SignupRequestDto;
import com.example.alba_pocket.service.KakaoService;
import com.example.alba_pocket.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto requestDto){
        return userService.signup(requestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response){
        return userService.login(requestDto, response);
    }

    @PostMapping("/kakao/callback")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return kakaoService.kakaoLogin(code, response);

    }




}
