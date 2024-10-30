package com.ukma.competition.platform.auth;

import com.ukma.competition.platform.auth.dto.LoginRequestDto;
import com.ukma.competition.platform.auth.dto.RegistrationRequestDto;
import jakarta.servlet.http.Cookie;

import java.util.List;

public interface AuthenticationService {

    List<Cookie> login(LoginRequestDto authDto);

    List<Cookie> register(RegistrationRequestDto authDto);
}
