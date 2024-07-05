package com.example.jwt.user.service;

import com.example.jwt.domain.Role;
import com.example.jwt.domain.RoleName;
import com.example.jwt.domain.User;
import com.example.jwt.domain.UserRole;
import com.example.jwt.role.RoleRepository;
import com.example.jwt.security.JwtTokenProvider;
import com.example.jwt.user.dto.SignInReqDto;
import com.example.jwt.user.dto.SignInResDto;
import com.example.jwt.user.dto.SignUpDto;
import com.example.jwt.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public String signUp(SignUpDto dto) {

        Optional<User> optionalUser = userRepository.findByUserId(dto.getUserId());
        if (optionalUser.isPresent()) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        List<Role> roles = dto.getUserRoles().stream()
                .map(roleName -> roleRepository.findByRoleName(RoleName.valueOf(roleName))
                        .orElseThrow(() -> new IllegalArgumentException(roleName + "은 존재하지 않는 역할입니다.")))
                .collect(Collectors.toList());

        User user = User.toEntity(dto, roles);
        userRepository.save(user);

        // 디버깅용 코드
        user.getUserRoles().forEach(userRole -> System.out.println("Saved role for user: " + userRole.getRole().getRoleName()));

        return "회원가입에 성공했습니다.";
    }

    public SignInResDto signIn(SignInReqDto dto) {
        User user = userRepository.findByUserId(dto.getUserId()).orElseThrow(RuntimeException::new);
        List<Role> roles = user.getUserRoles().stream()
                .map(UserRole::getRole)
                .collect(Collectors.toList());

        String token = jwtTokenProvider.createToken(user.getUserId(), roles);
        return SignInResDto.builder().accessToken(token).build();
    }
}
