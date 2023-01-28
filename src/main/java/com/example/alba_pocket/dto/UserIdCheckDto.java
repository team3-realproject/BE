package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UserIdCheckDto {
    @Email(message = "Email형식이 아닙니다.")
    @NotBlank(message = "Email을 입력해 주세요")
    String userId;
}
