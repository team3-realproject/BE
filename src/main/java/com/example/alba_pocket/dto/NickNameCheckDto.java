package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class NickNameCheckDto {
    @Pattern(regexp = "^[A-za-z0-9가-힣]{5,10}$", message = "닉네임은 5~10글자의 영어대소문자, 숫자, 한글만 입력할 수 있습니다.")
    String nickname;
}
