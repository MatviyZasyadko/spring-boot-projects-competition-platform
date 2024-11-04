package com.ukma.competition.platform.auth.oauth;

import com.ukma.competition.platform.auth.EndpointConstants;
import jakarta.servlet.http.Cookie;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractOAuth2Service {

    String REDIRECT_URI_PREFIX = "/api/oauth/callback/";

    public abstract String generateAuthenticationRedirectUrl();

    public abstract Cookie authenticationCallback(String code);

    public abstract AuthenticationProvider getOauthAuthenticationProvider();

    protected String buildApplicationRedirectUrl() {
        return EndpointConstants.getContextPath()
               + REDIRECT_URI_PREFIX
               + getOauthAuthenticationProvider().toString().toLowerCase();
    }
}
