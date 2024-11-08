package com.ukma.competition.platform.auth;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CookieServiceImpl implements CookieService {

    @Override
    public Cookie generateSecuredHttpOnlyCookie(String name, String value, Duration duration) {
        Cookie accessTokenCookie = new Cookie(name, value);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setMaxAge((int) duration.toMillis());
        accessTokenCookie.setPath("/");
        accessTokenCookie.setDomain("localhost");

        return accessTokenCookie;
    }
}
