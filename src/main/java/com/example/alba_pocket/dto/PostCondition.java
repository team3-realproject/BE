package com.example.alba_pocket.dto;

import com.example.alba_pocket.model.PostSearchKeyword;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCondition {
    //category
    private String category;
    //search
    private String keyword;
    //getMypage
    private Long userId;
}
