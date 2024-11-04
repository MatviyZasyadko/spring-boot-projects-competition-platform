package com.ukma.competition.platform.auth.oauth;

public interface OAuth2ServiceFactory {
    AbstractOAuth2Service get(AuthenticationProvider provider);
}
