package com.example.alba_pocket.service;

import com.example.alba_pocket.dto.KakaoUserInfoDto;
import com.example.alba_pocket.dto.UserResponseDto;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.jwt.JwtUtil;
import com.example.alba_pocket.repository.UserRepository;
import com.example.alba_pocket.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class KakaoService {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirect_uri;
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String user_info_uri;
    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String token_uri;


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;



    public ResponseEntity<?> kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        String accessToken = issuedAccessToken(code);

        //access_token??? ?????? ????????? ??????????????????
        KakaoUserInfoDto kakaoUserInfoDto = getKakaoUserInfo(accessToken);

        //?????????????????? ????????? ??????????????????(?????? DB??? ????????? ??????????????? ?????????)
        User user = saveUser(kakaoUserInfoDto,accessToken);

        //?????? ????????? ??????
        forceLoginUser(user);

        //????????????, ????????? ?????? ?????????
        //?????? ????????? response
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUserId()));

//        //?????? ???????????? ?????? ?????? ??????
//        Long notificationNum = notificationRepository.countUnReadStateNotifications(member.getMemberId());
//
//        UserInfoDto userInfoDto = new UserInfoDto(member,notificationNum);

        return new ResponseEntity<>(new UserResponseDto.LoginResponseDto(user.getUserId(), user.getNickname()), HttpStatus.OK);
    }

    private void forceLoginUser(User user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserId());
        Authentication createAuthentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(createAuthentication);
    }

    //??????????????? ?????? access_token ????????????
    public String issuedAccessToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body ??????
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", redirect_uri);
        body.add("code", code);


        // HTTP ?????? ?????????
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                token_uri,
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP ?????? (JSON) -> ????????? ?????? ??????
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String accessToken = jsonNode.get("access_token").asText();

        return accessToken;
    }

    //access_token??? ?????? ????????? ??????????????????
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header ??????
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP ?????? ?????????
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                user_info_uri,
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String email=null;
        try {
            email = jsonNode.get("kakao_account")
                    .get("email").asText();
        } catch (Exception e) {
            email = jsonNode.get("id").asLong()+"@kakaoEmail.com";
        }
        String userImgUrl = jsonNode.get("properties")
                .get("profile_image").asText();

        return new KakaoUserInfoDto(id, email, userImgUrl);
    }

    //?????????????????? ????????? ??????????????????(?????? DB??? ????????? ??????????????? ?????????)
    public User saveUser(KakaoUserInfoDto kakaoUserInfoDto,String accessToken) {
        User kakaoUser = userRepository.findByUserId("k_"+kakaoUserInfoDto.getEmail()).orElse(null);

        //????????? ??????
        if (kakaoUser == null) {
            User user = User.builder().
                    kakaoEmail("k_" + kakaoUserInfoDto.getEmail())
                    .nickname("kakao_"+(String.valueOf(kakaoUserInfoDto.getId())))
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .profileImage(kakaoUserInfoDto.getUserImgUrl())
                    .build();

            userRepository.save(user);
            return user;
        }

        //????????? member ??????
        return kakaoUser;
    }

//    public void createToken(User user,HttpServletResponse response){
//        TokenDto tokenDto = jwtUtil.createAllToken(member.getEmail());
//
//        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(member.getEmail());
//
//        if (refreshToken.isPresent()) {
//            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
//        } else {
//            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), member.getEmail());
//            refreshTokenRepository.save(newToken);
//        }
//
//        setHeader(response, tokenDto);
//    }

//    public GlobalResDto<?> logoutKakao(Member member){
//
//        //?????? ?????? ????????????
//        SocialAccessToken accessToken = socialAccessTokenRepository.findByAccountEmail(member.getEmail()).orElseThrow(()-> new CustomException(ErrorCode.ERROR));
//
//        //???????????? ???????????? ???????????? ??????
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken.getAccessToken());
//        headers.add("Content-type", "application/x-www-form-urlencoded");
//
//        // HTTP ?????? ?????????
//        HttpEntity<MultiValueMap<String, String>> kakaoLogoutRequest = new HttpEntity<>(headers);
//        RestTemplate rt = new RestTemplate();
//        try {
//            ResponseEntity<String> response = rt.exchange(
//                    "https://kapi.kakao.com/v1/user/logout",
//                    HttpMethod.POST,
//                    kakaoLogoutRequest,
//                    String.class
//            );
//        }catch (Exception e){
//            throw new CustomException(ErrorCode.ERROR);
//        }
//
//        socialAccessTokenRepository.delete(accessToken);
//
//        return GlobalResDto.success(null, "???????????? ??????");
//    }

}
