package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.MsgResponseDto;
import com.example.alba_pocket.dto.UserRequestDto;
import com.example.alba_pocket.dto.UserResponseDto;
import com.example.alba_pocket.entity.EmailCheck;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.errorcode.UserStatusCode;
import com.example.alba_pocket.exception.RestApiException;
import com.example.alba_pocket.jwt.JwtUtil;
import com.example.alba_pocket.repository.EmailCheckRepository;
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

    private final EmailCheckRepository emailCheckRepository;
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
    @Transactional
    public ResponseEntity<?> emailSend(UserRequestDto.EmailSendDto emailSendDto) throws MessagingException, UnsupportedEncodingException {
        String authCode = emailService.sendEmail(emailSendDto.getEmail());
        EmailCheck emailCheck = emailCheckRepository.findByEmail(emailSendDto.getEmail()).orElse(new EmailCheck());
        if(emailCheck.getEmail() != null){
            emailCheck.codeUpdate(authCode);
            return new ResponseEntity<>(new MsgResponseDto("인증코드 전송완료"), HttpStatus.OK);
        }
        EmailCheck email = new EmailCheck(emailSendDto.getEmail(), authCode);
        emailCheckRepository.save(email);

        return new ResponseEntity<>(new MsgResponseDto("인증코드 전송완료"), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> emailCheck(UserRequestDto.EmailCheckDto emailCheckDto) {
        EmailCheck emailCheck = emailCheckRepository.findByEmail(emailCheckDto.getEmail()).orElse(new EmailCheck());
        if(emailCheck.getEmail() == null){
            throw new RestApiException(UserStatusCode.NOT_FOUND_EMAIL_CODE);
        }
        if(!emailCheck.getAuthCode().equals(emailCheckDto.getCode())){
            throw new RestApiException(UserStatusCode.NOT_EMAIL_CODE);
        }
        emailCheckRepository.deleteById(emailCheck.getId());
        return new ResponseEntity<>(new MsgResponseDto("이메일 인증완료"), HttpStatus.OK);
    }
}
