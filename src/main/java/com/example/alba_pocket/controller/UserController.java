package com.example.alba_pocket.controller;

import com.example.alba_pocket.dto.LoginRequestDto;
import com.example.alba_pocket.dto.NickNameCheckDto;
import com.example.alba_pocket.dto.SignupRequestDto;
import com.example.alba_pocket.dto.UserIdCheckDto;
import com.example.alba_pocket.service.KakaoService;
import com.example.alba_pocket.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequestDto requestDto){
        return userService.signup(requestDto);
    }


    @PostMapping("/userid")
    public ResponseEntity<?>  userCheck(@RequestBody @Valid UserIdCheckDto userIdCheckDto) { return userService.userIdCheck(userIdCheckDto); }

    @PostMapping("/nickname")
    public ResponseEntity<?> nicknameCheck(@RequestBody @Valid NickNameCheckDto nickNameCheckDto) { return userService.nicknameCheck(nickNameCheckDto);}


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response){
        log.info("login-------------");
        return userService.login(requestDto, response);
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return kakaoService.kakaoLogin(code, response);

    }




}
