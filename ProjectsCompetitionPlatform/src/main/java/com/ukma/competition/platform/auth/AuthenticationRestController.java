package com.ukma.competition.platform.auth;

import com.ukma.competition.platform.auth.dto.LoginRequestDto;
import com.ukma.competition.platform.auth.dto.RegistrationRequestDto;
import com.ukma.competition.platform.shared.dto.exception.ExceptionDto;
import com.ukma.competition.platform.users.dto.UserResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationRestController {

    AuthenticationService authenticationService;

    @GetMapping("/profile")
    public UserResponseDto profile(HttpServletResponse response) {
        try {
            log.info("started process of getting a user profile with id");
            Authentication principal = SecurityContextHolder.getContext().getAuthentication();
            Optional<? extends GrantedAuthority> authorityOptional = principal.getAuthorities().stream().findFirst();
            String authority = null;

            if (authorityOptional.isPresent()) {
                authority = authorityOptional.get().getAuthority();
            } else {
                log.warn("User has null authority!");
            }

            return UserResponseDto.builder()
                .id(principal.getName())
                .email(principal.getName())
                .role(authority)
                .build();
        } catch (Exception exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        return UserResponseDto.builder().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/profile/admin")
    public UserResponseDto adminProfile(HttpServletResponse response) {
        try {
            Authentication principal = SecurityContextHolder.getContext().getAuthentication();
            Optional<? extends GrantedAuthority> authorityOptional = principal.getAuthorities().stream().findFirst();
            String authority = null;

            if (authorityOptional.isPresent()) {
                authority = authorityOptional.get().getAuthority();
            }

            return UserResponseDto.builder()
                .id(principal.getName())
                .email(principal.getName())
                .role(authority)
                .build();
        } catch (Exception exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        return UserResponseDto.builder().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
        @RequestBody @Valid LoginRequestDto loginRequestDto,
        HttpServletResponse response
    ) {
        try {
            response.addCookie(authenticationService.login(loginRequestDto));
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            return ResponseEntity.badRequest()
                .body(ExceptionDto.builder()
                    .message(exception.getMessage())
                    .exceptionClass(exception.getClass().getName())
                    .exceptionTime(Instant.now())
                    .build());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
        @RequestBody @Valid RegistrationRequestDto registrationRequestDto,
        HttpServletResponse response
    ) {
        try {
            response.addCookie(authenticationService.register(registrationRequestDto));
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            return ResponseEntity.badRequest()
                .body(ExceptionDto.builder()
                    .message(exception.getMessage())
                    .exceptionClass(exception.getClass().getName()).build());
        }
    }
}
