package com.example.alba_pocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class PostRequestDto {
    private MultipartFile file;
    private String title;
    private String content;
    private String category;
}