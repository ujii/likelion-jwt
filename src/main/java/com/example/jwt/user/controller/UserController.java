package com.example.jwt.user.controller;

import com.example.jwt.user.dto.SignInReqDto;
import com.example.jwt.user.dto.SignInResDto;
import com.example.jwt.user.dto.SignUpDto;
import com.example.jwt.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpDto dto) {
        String result = userService.signUp(dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInReqDto dto) {
        SignInResDto result = userService.signIn(dto);
        return ResponseEntity.ok(result);
    }
}
