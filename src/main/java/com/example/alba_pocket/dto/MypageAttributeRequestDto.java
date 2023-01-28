package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class MypageAttributeRequestDto {
    private MultipartFile file;
    @Pattern(regexp = "^[A-za-z0-9가-힣]{5,10}$", message = "닉네임은 5~10글자의 영어대소문자, 숫자, 한글만 입력할 수 있습니다.")
    private String nickname;

}
