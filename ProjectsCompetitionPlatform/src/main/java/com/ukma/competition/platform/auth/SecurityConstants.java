package com.ukma.competition.platform.auth;

import java.util.List;

public class SecurityConstants {

    private SecurityConstants() {}

    public static List<String> PUBLIC_ENDPOINTS = List.of(
        "/resources/**",
        "/*.css",
        "/*.js",
        "/ui/login",
        "/ui/registration",
        "/favicon.ico",
        "/content/**"
    );
}
