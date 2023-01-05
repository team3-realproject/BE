package com.example.alba_pocket.controller;

import com.example.alba_pocket.dto.LoginRequestDto;
import com.example.alba_pocket.dto.NickNameCheckDto;
import com.example.alba_pocket.dto.SignupRequestDto;
import com.example.alba_pocket.dto.UserIdCheckDto;
import com.example.alba_pocket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequestDto requestDto){
        return userService.signup(requestDto);
    }


    @PostMapping("/userid")
    public ResponseEntity<?>  userCheck(@RequestBody UserIdCheckDto userIdCheckDto) { return userService.userIdCheck(userIdCheckDto); }

    @PostMapping("/nickname")
    public ResponseEntity<?> nicknameCheck(@RequestBody NickNameCheckDto nickNameCheckDto) { return userService.nicknameCheck(nickNameCheckDto);}


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response){
        return userService.login(requestDto, response);
    }




}
