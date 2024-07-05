package com.example.jwt.post.controller;

import com.example.jwt.post.dto.CreatePostReq;
import com.example.jwt.post.dto.CreatePostRes;
import com.example.jwt.post.service.PostService;
import com.example.jwt.util.user.AuthenticationUserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;
    private final AuthenticationUserUtils userUtils;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid CreatePostReq dto) {
        String currentUserId = userUtils.getCurrentUserId();
        CreatePostRes result = postService.create(dto, currentUserId);
        return ResponseEntity.ok(result);
    }
}
