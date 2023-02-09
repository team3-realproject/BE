package com.example.alba_pocket.controller;

import com.example.alba_pocket.dto.*;
import com.example.alba_pocket.service.KakaoService;
import com.example.alba_pocket.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid UserRequestDto.SignupRequestDto requestDto){
        return userService.signup(requestDto);
    }


    @PostMapping("/userid")
    public ResponseEntity<?>  userCheck(@RequestBody @Valid UserRequestDto.UserIdCheckDto userIdCheckDto) { return userService.userIdCheck(userIdCheckDto); }

    @PostMapping("/nickname")
    public ResponseEntity<?> nicknameCheck(@RequestBody @Valid UserRequestDto.NickNameCheckDto nickNameCheckDto) { return userService.nicknameCheck(nickNameCheckDto);}


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDto.LoginRequestDto requestDto, HttpServletResponse response){
        log.info("login-------------");
        return userService.login(requestDto, response);
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return kakaoService.kakaoLogin(code, response);

    }

    //email 인증
    @PostMapping("/email")
    public ResponseEntity<?> emailSend(@RequestBody UserRequestDto.EmailSendDto emailSendDto) throws MessagingException, UnsupportedEncodingException {
        return userService.emailSend(emailSendDto);
    }

    @PostMapping("/emailcheck")
    public ResponseEntity<?> emailCheck(@RequestBody UserRequestDto.EmailCheckDto emailCheckDto){
        return userService.emailCheck(emailCheckDto);
    }
}
