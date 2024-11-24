package com.ukma.competition.platform.auth;

import com.ukma.competition.platform.shared.constants.AppConstants;
import com.ukma.competition.platform.users.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class JwtService {

    Duration JWT_ACCESS_TOKEN_EXPIRATION_DURATION;
    Duration JWT_REFRESH_TOKEN_EXPIRATION_DURATION;
    AuthenticationKeyProvider keyProvider;

    public JwtService(
        @Value("${jwt.expiration.duration.access}") Duration JWT_ACCESS_TOKEN_EXPIRATION_DURATION,
        @Value("${jwt.expiration.duration.refresh}") Duration JWT_REFRESH_TOKEN_EXPIRATION_DURATION,
        AuthenticationKeyProvider keyProvider
    ) {
        this.JWT_ACCESS_TOKEN_EXPIRATION_DURATION = JWT_ACCESS_TOKEN_EXPIRATION_DURATION;
        this.JWT_REFRESH_TOKEN_EXPIRATION_DURATION = JWT_REFRESH_TOKEN_EXPIRATION_DURATION;
        this.keyProvider = keyProvider;
    }

    public String generateAccessTokenWithClaims(Map<String, Object> claims, String subject) {
        return generateToken(
            claims,
            subject,
            Instant.now().plus(JWT_ACCESS_TOKEN_EXPIRATION_DURATION)
        );
    }

    private String generateToken(Map<String, Object> claims, String subject, Instant expirationDate) {
        return Jwts.builder()
            .claims(claims)
            .subject(subject)
            .issuedAt(new Date())
            .expiration(Date.from(expirationDate))
            .signWith(keyProvider.getPrivateKey(), Jwts.SIG.RS256)
            .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(keyProvider.getPublicKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public String generateTokenFromUser(UserEntity user) {
        return this.generateAccessTokenWithClaims(
            Map.of(
                "role", user.getUserRole().name(),
                "authProvider", user.getAuthenticationProvider().toString(),
                "fullName", user.getFullName()
            ),
            user.getUsername()
        );
    }

    public Cookie generateTokenWithCookie(UserEntity user) {
        String accessToken = this.generateTokenFromUser(user);

        Cookie accessTokenCookie = new Cookie(AppConstants.ACCESS_TOKEN_NAME, accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setMaxAge((int) JWT_ACCESS_TOKEN_EXPIRATION_DURATION.toMillis());
        accessTokenCookie.setPath("/");
        accessTokenCookie.setDomain("localhost");

        return accessTokenCookie;
    }

    public Duration getJwtAccessTokenExpirationDuration() {
        return JWT_ACCESS_TOKEN_EXPIRATION_DURATION;
    }
}