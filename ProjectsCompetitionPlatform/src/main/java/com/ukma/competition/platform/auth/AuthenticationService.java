package com.ukma.competition.platform.auth;

import com.ukma.competition.platform.auth.dto.AuthDto;
import jakarta.servlet.http.Cookie;

import java.util.List;

public interface AuthenticationService {

    List<Cookie> login(AuthDto authDto);

    List<Cookie> register(AuthDto authDto);
}
