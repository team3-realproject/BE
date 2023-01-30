package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class NickNameCheckDto {
    @Pattern(regexp = "^[A-za-z0-9가-힣]{5,10}$", message = "영문 대소문자, 글자 단위 한글, 숫자를 사용하여 5~10글자를 적어주세요.")
    String nickname;
}
