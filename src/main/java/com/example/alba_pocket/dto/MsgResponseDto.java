package com.example.alba_pocket.dto;

import com.example.alba_pocket.errorcode.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MsgResponseDto {
    private String msg;

    public MsgResponseDto(String msg) {
        this.msg = msg;
    }

    public MsgResponseDto(StatusCode statusCode){
        this.msg = statusCode.getMsg();
    }


}
