package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.*;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.errorcode.UserStatusCode;
import com.example.alba_pocket.exception.RestApiException;
import com.example.alba_pocket.jwt.JwtUtil;
import com.example.alba_pocket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService emailService;
    private final JwtUtil jwtUtil;
    @Transactional
    public ResponseEntity<?> signup(UserRequestDto.SignupRequestDto requestDto) {
        String password = passwordEncoder.encode(requestDto.getPassword());
        if(userRepository.existsByUserId(requestDto.getUserId())) {
            throw new RestApiException(UserStatusCode.OVERLAPPED_USERID);
        }
        if(userRepository.existsByNickname(requestDto.getNickname())) {
            throw new RestApiException(UserStatusCode.OVERLAPPED_NICKNAME);
        }

        User user = new User(requestDto, password);
        userRepository.saveAndFlush(user);

        return new ResponseEntity<>(new MsgResponseDto("회원가입성공"), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> login(UserRequestDto.LoginRequestDto requestDto, HttpServletResponse response) {
        User user = userRepository.findByUserId(requestDto.getUserId()).orElseThrow(
                () -> new RestApiException(UserStatusCode.NOT_FOUND_USERID)
        );
        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            throw new RestApiException(UserStatusCode.PASSWORD_CHECK);
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUserId()));

        return new ResponseEntity<>(new UserResponseDto.LoginResponseDto(user.getUserId(), user.getNickname()), HttpStatus.OK);



    }

    @Transactional
    public ResponseEntity<?> userIdCheck(UserRequestDto.UserIdCheckDto userIdCheckDto) {
        if(userRepository.existsByUserId(userIdCheckDto.getUserId())) {
            throw new RestApiException(UserStatusCode.OVERLAPPED_USERID);
        } else {
            return new ResponseEntity<>(new MsgResponseDto("사용 가능한 ID 입니다."), HttpStatus.OK);
        }
    }

    @Transactional
    public ResponseEntity<?> nicknameCheck(UserRequestDto.NickNameCheckDto nickNameCheckDto) {
        if(userRepository.existsByNickname(nickNameCheckDto.getNickname())) {
            throw new RestApiException(UserStatusCode.OVERLAPPED_NICKNAME);
        } else {
            return new ResponseEntity<>(new MsgResponseDto("사용 가능한 닉네임 입니다."), HttpStatus.OK);
        }
    }
    //email 인증
    public ResponseEntity<?> emailCheck(UserRequestDto.EmailCheckDto emailCheckDto) throws MessagingException, UnsupportedEncodingException {
        String authCode = emailService.sendEmail(emailCheckDto.getEmail());
        return new ResponseEntity<>(new MsgResponseDto(authCode), HttpStatus.OK);
    }
}
