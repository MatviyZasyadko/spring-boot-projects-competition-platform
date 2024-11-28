package com.ukma.competition.platform.auth;

import com.ukma.competition.platform.shared.constants.AppConstants;
import com.ukma.competition.platform.users.UserEntity;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.PathContainer;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    JwtService jwtService;
    UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return EndpointConstants.PUBLIC_ENDPOINTS.stream().anyMatch(pattern -> this.uriMatches(pattern, request.getRequestURI()));
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            if (request.getCookies() != null) {
                Cookie accessTokenCookie = Arrays.stream(request.getCookies())
                    .filter(item -> item.getName().contains(AppConstants.accessTokenName))
                    .findFirst()
                    .orElse(null);
                if (accessTokenCookie != null) {
                    String token = accessTokenCookie.getValue();
                    Claims claims = jwtService.extractAllClaims(token);
                    UserEntity user = (UserEntity) this.userDetailsService.loadUserByUsername(claims.getSubject());
                    UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(
                        user,
                        null,
                        List.of(user.getUserRole())
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (Exception exception) {
            log.error("Error occurred while user authentication: {}.", exception.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private boolean uriMatches(String uriTemplate, String path) {
        PathPatternParser parser = new PathPatternParser();
        PathPattern p = parser.parse(uriTemplate);
        PathContainer pc = PathContainer.parsePath(path);
        return p.matches(pc);
    }
}