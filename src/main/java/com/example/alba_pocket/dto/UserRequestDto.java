package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class UserRequestDto {

    @Getter
    @NoArgsConstructor
    public static class SignupRequestDto {
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일을 입력해 주세요")
        private String userId;
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&].{8,20}$", message = "비밀번호는 알파벳 대소문자와 숫자, 특수문자로 구성된 8~20자리여야 합니다.")
        private String password;
        @Pattern(regexp = "^[A-za-z0-9가-힣]{5,10}$", message = "영문 대소문자, 글자 단위 한글, 숫자를 사용하여 5~10글자를 적어주세요.")
        private String nickname;
    }

    @Getter
    @NoArgsConstructor
    public static class LoginRequestDto {
        private String userId;
        private String password;
    }

    @Getter
    @NoArgsConstructor
    public static class UserIdCheckDto {
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일을 입력해 주세요")
        String userId;
    }

    @Getter
    @NoArgsConstructor
    public static class NickNameCheckDto {
        @Pattern(regexp = "^[A-za-z0-9가-힣]{5,10}$", message = "영문 대소문자, 글자 단위 한글, 숫자를 사용하여 5~10글자를 적어주세요.")
        String nickname;
    }

    @Getter
    @NoArgsConstructor
    public static class EmailCheckDto {
        public String email;
    }


}
