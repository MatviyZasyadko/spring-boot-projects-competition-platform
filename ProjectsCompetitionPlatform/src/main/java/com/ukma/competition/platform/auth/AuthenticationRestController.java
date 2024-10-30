package com.ukma.competition.platform.auth;

import com.ukma.competition.platform.auth.dto.LoginRequestDto;
import com.ukma.competition.platform.auth.dto.RegistrationRequestDto;
import com.ukma.competition.platform.users.dto.UserResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationRestController {

    AuthenticationService authenticationService;

    @GetMapping("/profile")
    public UserResponseDto profile(HttpServletResponse response) {
        try {
            Authentication principal = SecurityContextHolder.getContext().getAuthentication();

            return UserResponseDto.builder()
                .id(principal.getName())
                .email(principal.getName())
                .role(principal.getAuthorities().stream().findFirst().get().getAuthority())
                .build();
        } catch (Exception exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        return UserResponseDto.builder().build();
    }

    @GetMapping("/profile/admin")
    public UserResponseDto adminProfile(HttpServletResponse response) {
        try {
            Authentication principal = SecurityContextHolder.getContext().getAuthentication();

            return UserResponseDto.builder()
                .id(principal.getName())
                .email(principal.getName())
                .role(principal.getAuthorities().stream().findFirst().get().getAuthority())
                .build();
        } catch (Exception exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        return UserResponseDto.builder().build();
    }

    @PostMapping("/login")
    public void login(
        @RequestBody @Valid LoginRequestDto loginRequestDto,
        HttpServletResponse response
    ) {
        try {
            authenticationService.login(loginRequestDto).forEach(response::addCookie);
        } catch (Exception exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }

    @PostMapping("/register")
    public void register(
        @RequestBody @Valid RegistrationRequestDto registrationRequestDto,
        HttpServletResponse response
    ) {
        try {
            authenticationService.register(registrationRequestDto).forEach(response::addCookie);
        } catch (Exception exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }
}
