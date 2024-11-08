package com.ukma.competition.platform.auth;

import jakarta.servlet.http.Cookie;

import java.time.Duration;

public interface CookieService {

    Cookie generateSecuredHttpOnlyCookie(String name, String value, Duration expirationTime);
}
