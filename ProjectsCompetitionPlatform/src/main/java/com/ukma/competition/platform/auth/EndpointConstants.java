package com.ukma.competition.platform.auth;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

public class EndpointConstants {

    private EndpointConstants() {}

    public static final String LOGIN_PAGE_ENDPOINT = "/ui/login";

    public static String getContextPath() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }

    public static final List<String> PUBLIC_ENDPOINTS = List.of(
        "/resources/**",
        "/*.css",
        "/*.js",
        "/*.jpg",
        "/*.png",
        "/error.html",
        LOGIN_PAGE_ENDPOINT,
        "/ui/registration",
        "/favicon.ico",
        "/content/**",
        "/api/oauth/**",
        "/api/auth/login",
        "/api/auth/register"
    );
}
