package com.example.alba_pocket.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SecurityExceptionCode implements StatusCode{
    private final String Msg;
    private final int statusCode;
}
