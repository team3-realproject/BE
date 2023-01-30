package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Greeting {

    private String content;

    public Greeting(String content) {
        this.content = content;
    }


}
