package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.*;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.jwt.JwtUtil;
import com.example.alba_pocket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;
    @Transactional
    public ResponseEntity<?> signup(SignupRequestDto requestDto) {
        String password = passwordEncoder.encode(requestDto.getPassword());
        User user = new User(requestDto, password);
        userRepository.saveAndFlush(user);

        return new ResponseEntity<>(new MsgResponseDto("회원가입성공"), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
        User user = userRepository.findByUserId(requestDto.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("가입된 이메일 주소가 아닙니다.")
        );
        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUserId()));

        return new ResponseEntity<>(new LoginResponseDto(user.getNickname()), HttpStatus.OK);



    }

    @Transactional
    public ResponseEntity<?> userIdCheck(UserIdCheckDto userIdCheckDto) {
        if(userRepository.existsByUserId(userIdCheckDto.getUserId())) {
            throw new IllegalArgumentException("이미 가입된 이메일 주소입니다.");
        } else {
            return new ResponseEntity<>(new MsgResponseDto("사용 가능한 ID 입니다."), HttpStatus.OK);
        }
    }

    @Transactional
    public ResponseEntity<?> nicknameCheck(NickNameCheckDto nickNameCheckDto) {
        if(userRepository.existsByNickname(nickNameCheckDto.getNickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        } else {
            return new ResponseEntity<>(new MsgResponseDto("사용 가능한 닉네임 입니다."), HttpStatus.OK);
        }
    }

}
