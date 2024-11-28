package com.ukma.competition.platform.auth;

import com.ukma.competition.platform.auth.dto.LoginRequestDto;
import com.ukma.competition.platform.auth.dto.RegistrationRequestDto;
import jakarta.servlet.http.Cookie;

public interface AuthenticationService {

    Cookie login(LoginRequestDto authDto);

    Cookie register(RegistrationRequestDto authDto);

    Cookie logout();
}
