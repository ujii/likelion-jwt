package com.example.jwt.domain;

import com.example.jwt.user.dto.SignUpDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USERS")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<UserRole> userRoles;

    public static User toEntity(SignUpDto dto, List<Role> roles) {

        User user = User.builder()
                .userId(dto.getUserId())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .build();

        List<UserRole> userRoles = dto.getUserRoles().stream()
                .map(roleName -> roles.stream()
                        .filter(role -> role.getRoleName().name().equals(roleName))
                        .findFirst()
                        .map(role -> new UserRole(null, user, role))
                        .orElseThrow(() -> new IllegalArgumentException(roleName + "는 존재하지 않는 역할입니다.")))
                .collect(Collectors.toList());

        user.userRoles = userRoles;

        return user;
    }
}
