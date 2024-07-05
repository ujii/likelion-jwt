package com.example.jwt.security;

import com.example.jwt.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final MyUserDetails myUserDetails;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long validityInMilliseconds;

    @PostConstruct
    protected void init() {
    }

    public String createToken(String username, List<Role> roles) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", roles.stream()
                .map(role -> role.getRoleName().getAuthority())
                .collect(Collectors.toList()));

        Date now = new Date();
        Date validaty = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validaty)
                .signWith(getSignKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey(String secretKey) {

        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public void setSecurityContext(String token) {

        Claims claims = getClaimsFromToken(token);

        List<String> roles = claims.get("auth", List.class);
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                claims.getSubject(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);

    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey(secretKey)).build().parseClaimsJws(token).getBody().getSubject();
    }

    // 이 메소드 반환형 Claims
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey(secretKey)).build().parseClaimsJws(token).getBody();
    }

    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(getSignKey(secretKey)).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalStateException e) {
            throw new RuntimeException("만료되었거나 유효하지 않은 JWT 토큰입니다.");
        }
    }

}
