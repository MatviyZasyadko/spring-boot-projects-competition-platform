package com.ukma.competition.platform.auth;

import com.ukma.competition.platform.auth.dto.AuthDto;
import com.ukma.competition.platform.shared.exception.AuthenticationException;
import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.users.UserRepository;
import com.ukma.competition.platform.users.UserRole;
import jakarta.servlet.http.Cookie;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    JwtService jwtService;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public List<Cookie> login(AuthDto authDto) {
        UserEntity userCheck = userRepository.findByEmail(authDto.getEmail()).orElse(null);
        if (userCheck != null) {
            if (passwordEncoder.matches(authDto.getPassword(), userCheck.getPassword())) {
                return generateTokenPair(userCheck);
            }
        }
        throw new AuthenticationException("Username or password is not correct!");
    }


    public List<Cookie> register(AuthDto authDto) {
        Optional<UserEntity> userCheck = userRepository.findByEmail(authDto.getEmail());
        if (userCheck.isEmpty()) {
            UserEntity newUser = UserEntity.builder()
                .email(authDto.getEmail())
                .password(passwordEncoder.encode(authDto.getPassword()))
                .userRole(UserRole.USER)
                .build();
            userRepository.save(newUser);

            return generateTokenPair(newUser);
        } else {
            throw new AuthenticationException("user is already exists");
        }
    }

    private List<Cookie> generateTokenPair(UserEntity user) {
        List<Cookie> cookies = new ArrayList<>();

        String accessToken = jwtService.generateAccessToken(
            Map.of("role", user.getUserRole().name()),
            user.getUsername()
        );
        String refreshToken = jwtService.generateRefreshToken(
            Map.of("role", user.getUserRole().name()),
            user.getUsername()
        );

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);

        return List.of(accessTokenCookie, refreshTokenCookie);
    }
}