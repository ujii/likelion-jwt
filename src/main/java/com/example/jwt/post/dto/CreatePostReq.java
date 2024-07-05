package com.example.jwt.post.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreatePostReq {
    @NotNull
    private String title;
    @NotNull
    private String content;
}
