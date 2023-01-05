package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class SignupRequestDto {

    @Email(message = "Email형식이 아닙니다.")
    @NotBlank(message = "Email을 입력해 주세요")
    private String userId;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&].{8,20}$", message = "비밀번호는 알파벳 대소문자와 숫자, 특수문자로 구성된 8~20자리여야 합니다.")
    private String password;
    @Pattern(regexp = "^[A-za-z0-9가-힣]{5,10}$", message = "닉네임은 5~10글자의 영어대소문자, 숫자, 한글만 입력할 수 있습니다.")
    private String nickname;

}
