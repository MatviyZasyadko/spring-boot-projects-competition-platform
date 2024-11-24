package com.ukma.competition.platform.config;

import com.ukma.competition.platform.auth.CustomAuthenticationProvider;
import com.ukma.competition.platform.auth.JwtAuthenticationFilter;
import com.ukma.competition.platform.auth.EndpointConstants;
import com.ukma.competition.platform.users.UserRole;
import com.ukma.competition.platform.users.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    JwtAuthenticationFilter jwtAuthenticationFilter;
    CustomAuthenticationProvider authenticationProvider;
    UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests -> {
                EndpointConstants.PUBLIC_ENDPOINTS.forEach(endpoint -> requests.requestMatchers(endpoint).permitAll());
                requests.requestMatchers("/ui/**").authenticated();
                requests.requestMatchers("/api/**").authenticated();
                requests.anyRequest().permitAll();
            })
            .headers(headers -> headers.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
            .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(handler -> handler.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(EndpointConstants.LOGIN_PAGE_ENDPOINT)))
            .authenticationProvider(authenticationProvider)
            .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy(
            UserRole.ADMIN.getAuthority() + " > " + UserRole.USER.getAuthority()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}