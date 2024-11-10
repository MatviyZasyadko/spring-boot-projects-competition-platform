package com.ukma.competition.platform.auth.oauth;

import com.ukma.competition.platform.auth.EndpointConstants;
import com.ukma.competition.platform.auth.JwtService;
import com.ukma.competition.platform.auth.oauth.dto.OAuth2TokensResponseDto;
import com.ukma.competition.platform.images.ImageEntity;
import com.ukma.competition.platform.shared.exception.AuthenticationException;
import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.users.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@Slf4j
public abstract class AbstractOAuth2Service {

    String REDIRECT_URI_PREFIX = "/api/oauth/callback/";
    String EXTERNAL_AUTH_PAGE;
    String CLIENT_ID;
    String CLIENT_SECRET;
    String STATE;
    String SCOPE;

    UserService userService;
    JwtService jwtService;

    public AbstractOAuth2Service(
        String EXTERNAL_AUTH_PAGE,
        String CLIENT_ID,
        String CLIENT_SECRET,
        String STATE,
        String SCOPE,
        UserService userService,
        JwtService jwtService
    ) {
        this.EXTERNAL_AUTH_PAGE = EXTERNAL_AUTH_PAGE;
        this.CLIENT_ID = CLIENT_ID;
        this.CLIENT_SECRET = CLIENT_SECRET;
        this.STATE = STATE;
        this.SCOPE = SCOPE;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public abstract AuthenticationProvider getOAuth2AuthenticationProvider();

    public abstract OAuth2TokensResponseDto requestOAuth2Tokens(String code);

    public abstract OAuth2UserInfo getUserInfoFromResourceServer(String accessToken);

    public String buildAuthenticationRedirectUrl() {
        return "%s?client_id=%s&response_type=code&scope=%s&state=%s&redirect_uri=%s"
            .formatted(
                EXTERNAL_AUTH_PAGE,
                CLIENT_ID,
                URLEncoder.encode(SCOPE, StandardCharsets.UTF_8),
                STATE,
                URLEncoder.encode(buildApplicationRedirectUrl(), StandardCharsets.UTF_8)
            );
    }

    public String authenticationCallback(String code) {
        try {
            log.info("Starting the processing of OAuth2 callback for provider {}", this.getOAuth2AuthenticationProvider());
            OAuth2TokensResponseDto tokensResponseDto = requestOAuth2Tokens(code);
            OAuth2UserInfo userInfoFromResourceServer = getUserInfoFromResourceServer(tokensResponseDto.getAccessToken());

            validateUserInfo(userInfoFromResourceServer);

            UserEntity userCheck = this.userService.findByEmail(userInfoFromResourceServer.getEmail()).orElse(null);

            if (userCheck == null) {
                userCheck = UserEntity.builder()
                    .email(userInfoFromResourceServer.getEmail())
                    .fullName(userInfoFromResourceServer.getUsername())
                    .authenticationProvider(this.getOAuth2AuthenticationProvider())
                    .build();
                if (userInfoFromResourceServer.getAvatar() != null) {
                    ImageEntity image = ImageEntity.builder()
                        .url(userInfoFromResourceServer.getAvatar())
                        .build();
                    userCheck.addImage(image);
                }
                this.userService.save(userCheck);
            }

            return this.jwtService.generateTokenFromUser(userCheck);
        } catch (Exception exception) {
            log.error("Failed an attempt to authorize user through OAuth2 provider {}", this.getOAuth2AuthenticationProvider());
            throw new AuthenticationException("Some error occurred during OAuth2 authorization", exception);
        }
    }

    protected String buildApplicationRedirectUrl() {
        return EndpointConstants.getContextPath()
               + REDIRECT_URI_PREFIX
               + getOAuth2AuthenticationProvider().toString().toLowerCase();
    }

    private void validateUserInfo(OAuth2UserInfo googleUserInfoDto) {
        if (StringUtils.isBlank(googleUserInfoDto.getEmail()) || StringUtils.isBlank(googleUserInfoDto.getUsername())) {
            throw new AuthenticationException("OAuth2 error: user info from resource server does not contain necessary data!");
        }
    }
}
