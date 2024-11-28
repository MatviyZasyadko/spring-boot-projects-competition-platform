package com.ukma.competition.platform.auth;

import com.ukma.competition.platform.auth.dto.LoginRequestDto;
import com.ukma.competition.platform.auth.dto.RegistrationRequestDto;
import com.ukma.competition.platform.auth.oauth.AuthenticationProvider;
import com.ukma.competition.platform.shared.constants.AppConstants;
import com.ukma.competition.platform.shared.exception.AuthenticationException;
import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.users.UserRepository;
import com.ukma.competition.platform.users.UserRole;
import jakarta.servlet.http.Cookie;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    JwtService jwtService;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;


    public Cookie login(LoginRequestDto authDto) {
        UserEntity userCheck = userRepository.findByEmail(authDto.getEmail()).orElse(null);
        if (userCheck != null) {
            if (passwordEncoder.matches(authDto.getPassword(), userCheck.getPassword())) {
                return this.jwtService.generateTokenWithCookie(userCheck);
            }
        }
        throw new AuthenticationException("Username or password is not correct!");
    }

    public Cookie register(RegistrationRequestDto authDto) {
        Optional<UserEntity> userCheck = userRepository.findByEmail(authDto.getEmail());
        if (userCheck.isEmpty()) {
            UserEntity newUser = UserEntity.builder()
                .fullName(authDto.getFullName())
                .email(authDto.getEmail())
                .password(passwordEncoder.encode(authDto.getPassword()))
                .userRole(UserRole.USER)
                .authenticationProvider(AuthenticationProvider.NATIVE)
                .build();
            userRepository.save(newUser);

            return this.jwtService.generateTokenWithCookie(newUser);
        } else {
            throw new AuthenticationException("User with such email already exists");
        }
    }

    @Override
    public Cookie logout() {
        Cookie resetAccessTokenCookie = new Cookie(AppConstants.ACCESS_TOKEN_NAME, "");
        resetAccessTokenCookie.setHttpOnly(true);
        resetAccessTokenCookie.setSecure(true);
        resetAccessTokenCookie.setMaxAge(0);
        resetAccessTokenCookie.setPath("/");
        resetAccessTokenCookie.setDomain("localhost");
        return resetAccessTokenCookie;
    }

}