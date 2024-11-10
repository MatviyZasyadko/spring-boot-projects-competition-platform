package com.ukma.competition.platform.auth.oauth;

import com.ukma.competition.platform.auth.CookieService;
import com.ukma.competition.platform.auth.EndpointConstants;
import com.ukma.competition.platform.auth.JwtService;
import com.ukma.competition.platform.shared.annotations.PerformanceTracker;
import com.ukma.competition.platform.shared.constants.AppConstants;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/oauth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@PerformanceTracker
public class OAuth2RestController {

    OAuth2ServiceFactory oAuthServiceFactory;
    CookieService cookieService;
    JwtService jwtService;

    @GetMapping("/redirect/{provider}")
    public ResponseEntity<Void> redirectToAuthPage(
        @PathVariable("provider")
        String provider
    ) {
        try {
            String redirectUrl = oAuthServiceFactory.get(AuthenticationProvider.valueOf(provider.toUpperCase()))
                .buildAuthenticationRedirectUrl();
            return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(EndpointConstants.getContextPath() + EndpointConstants.LOGIN_PAGE_ENDPOINT))
                .build();
        }
    }

    @GetMapping("/callback/{provider}")
    public void callback(
        @PathVariable("provider")
        String provider,
        @RequestParam("code")
        String code,
        HttpServletResponse response
    ) throws IOException {
        String accessToken = oAuthServiceFactory.get(AuthenticationProvider.valueOf(provider.toUpperCase()))
            .authenticationCallback(code);
        response.addCookie(
            cookieService.generateSecuredHttpOnlyCookie(AppConstants.ACCESS_TOKEN_NAME, accessToken, this.jwtService.JWT_ACCESS_TOKEN_EXPIRATION_DURATION)
        );
        response.sendRedirect(EndpointConstants.getContextPath() + "/ui/main");
    }

    private URI buildExceptionOAuthRedirectUrl() {
        return URI.create(EndpointConstants.getContextPath()
                          + EndpointConstants.LOGIN_PAGE_ENDPOINT
                          + "?error="
                          + URLEncoder.encode("Error occurred during OAuth2 authorization", StandardCharsets.UTF_8)
        );
    }
}
