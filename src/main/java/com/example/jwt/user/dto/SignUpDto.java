package com.example.jwt.user.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpDto {
    @NotNull
    private String userId;
    @NotNull
    private String password;
    @NotNull
    private String email;
    @NotNull
    List<String> userRoles;

}
