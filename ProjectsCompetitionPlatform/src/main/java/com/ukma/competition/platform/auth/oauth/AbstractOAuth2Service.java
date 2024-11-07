package com.ukma.competition.platform.auth.oauth;

import com.ukma.competition.platform.auth.EndpointConstants;
import com.ukma.competition.platform.shared.annotations.PerformanceTracker;
import jakarta.servlet.http.Cookie;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractOAuth2Service {

    String REDIRECT_URI_PREFIX = "/api/oauth/callback/";
    String EXTERNAL_AUTH_PAGE;
    String CLIENT_ID;
    String CLIENT_SECRET;
    String STATE;
    String SCOPE;

    public AbstractOAuth2Service(
        String EXTERNAL_AUTH_PAGE,
        String CLIENT_ID,
        String CLIENT_SECRET,
        String STATE,
        String SCOPE
    ) {
        this.EXTERNAL_AUTH_PAGE = EXTERNAL_AUTH_PAGE;
        this.CLIENT_ID = CLIENT_ID;
        this.CLIENT_SECRET = CLIENT_SECRET;
        this.STATE = STATE;
        this.SCOPE = SCOPE;
    }

    public String generateAuthenticationRedirectUrl() {
        return "%s?client_id=%s&response_type=code&scope=%s&state=%s&redirect_uri=%s"
            .formatted(
                EXTERNAL_AUTH_PAGE,
                CLIENT_ID,
                URLEncoder.encode(SCOPE, StandardCharsets.UTF_8),
                STATE,
                URLEncoder.encode(buildApplicationRedirectUrl(), StandardCharsets.UTF_8)
            );
    }

    public abstract Cookie authenticationCallback(String code);

    public abstract AuthenticationProvider getOAuth2AuthenticationProvider();

    protected String buildApplicationRedirectUrl() {
        return EndpointConstants.getContextPath()
               + REDIRECT_URI_PREFIX
               + getOAuth2AuthenticationProvider().toString().toLowerCase();
    }
}
