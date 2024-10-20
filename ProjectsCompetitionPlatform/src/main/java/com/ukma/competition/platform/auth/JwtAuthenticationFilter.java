package com.ukma.competition.platform.auth;

import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.users.UserRole;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.server.PathContainer;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    JwtService jwtService;

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return SecurityConstants.PUBLIC_ENDPOINTS.stream().anyMatch(pattern -> this.uriMatches(pattern, request.getRequestURI()));
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getCookies() != null) {
            Cookie accessTokenCookie = Arrays.stream(request.getCookies())
                .filter(item -> item.getName().contains("accessToken"))
                .findFirst()
                .orElse(null);
            if (accessTokenCookie != null) {
                String token = accessTokenCookie.getValue();
                Claims claims = jwtService.extractAllClaims(token);
                UserRole role = claims.get("role") != null
                    ? (claims.get("role").equals("ADMIN") ? UserRole.ADMIN : UserRole.USER)
                    : UserRole.USER;
                List<GrantedAuthority> roleList = Collections.singletonList(role);
                UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(
                    UserEntity.builder().email(claims.getSubject()).userRole(role).build(),
                    null,
                    roleList
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean uriMatches(String uriTemplate, String path) {
        PathPatternParser parser = new PathPatternParser();
        PathPattern p = parser.parse(uriTemplate);
        PathContainer pc =  PathContainer.parsePath(path);
        return p.matches(pc);
    }
}