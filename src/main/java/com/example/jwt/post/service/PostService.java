package com.example.jwt.post.service;

import com.example.jwt.domain.Post;
import com.example.jwt.domain.User;
import com.example.jwt.post.dto.CreatePostReq;
import com.example.jwt.post.dto.CreatePostRes;
import com.example.jwt.post.repository.PostRepository;
import com.example.jwt.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreatePostRes create(CreatePostReq dto, String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("아이디가 " + userId + "인 회원은 존재하지 않습니다.");
        }

        User user = optionalUser.get();
        Post post = Post.toEntity(dto, user);
        Long postId = postRepository.save(post).getId();
        return CreatePostRes.builder().postId(postId).build();
    }
}
