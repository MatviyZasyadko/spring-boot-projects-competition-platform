package com.ukma.competition.platform.auth.oauth;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class OAuth2ServiceFactoryImpl implements OAuth2ServiceFactory {

    Map<AuthenticationProvider, AbstractOAuth2Service> serviceMap;

    public OAuth2ServiceFactoryImpl(List<AbstractOAuth2Service> oauthServices) {
        this.serviceMap = new HashMap<>();
        oauthServices.forEach(oauthService ->
            serviceMap.put(oauthService.getOauthAuthenticationProvider(), oauthService)
        );
    }

    public AbstractOAuth2Service get(AuthenticationProvider provider) {
        return serviceMap.get(provider);
    }
}
