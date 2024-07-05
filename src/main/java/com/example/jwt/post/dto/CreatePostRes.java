package com.example.jwt.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreatePostRes {
    private Long postId;
}
