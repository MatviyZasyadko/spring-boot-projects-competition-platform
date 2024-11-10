package com.ukma.competition.platform.auth.oauth;

import com.ukma.competition.platform.auth.JwtService;
import com.ukma.competition.platform.auth.oauth.dto.GitHubOAuth2EmailResponseDto;
import com.ukma.competition.platform.auth.oauth.dto.GitHubOAuth2UserInfoDto;
import com.ukma.competition.platform.auth.oauth.dto.OAuth2TokensRequestDto;
import com.ukma.competition.platform.auth.oauth.dto.OAuth2TokensResponseDto;
import com.ukma.competition.platform.users.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GitHubOAuth2Service extends AbstractOAuth2Service {

    String GITHUB_API_TOKEN_URL;
    String GITHUB_API_USER_INFO_BASE_URL;

    public GitHubOAuth2Service(
        @Value("${oauth2.provider.github.url.apis.token}") String GITHUB_API_TOKEN_BASE_URL,
        @Value("${oauth2.provider.github.url.apis.user-info}") String GITHUB_API_USER_INFO_BASE_URL,
        @Value("${oauth2.provider.github.url.authPage}") String EXTERNAL_AUTH_PAGE,
        @Value("${oauth2.provider.github.client.id}") String CLIENT_ID,
        @Value("${oauth2.provider.github.client.secret}") String CLIENT_SECRET,
        @Value("${oauth2.state}") String STATE,
        @Value("${oauth2.provider.github.scope}") String SCOPE,
        UserService userService,
        JwtService jwtService
    ) {
        super(
            EXTERNAL_AUTH_PAGE,
            CLIENT_ID,
            CLIENT_SECRET,
            STATE,
            SCOPE,
            userService,
            jwtService
        );
        this.GITHUB_API_TOKEN_URL = GITHUB_API_TOKEN_BASE_URL;
        this.GITHUB_API_USER_INFO_BASE_URL = GITHUB_API_USER_INFO_BASE_URL;
    }

    @Override
    public AuthenticationProvider getOAuth2AuthenticationProvider() {
        return AuthenticationProvider.GITHUB;
    }

    @Override
    public OAuth2TokensResponseDto requestOAuth2Tokens(String code) {
        RestClient restClient = RestClient.create();

         return restClient.post()
            .uri(GITHUB_API_TOKEN_URL)
            .body(buildOAuth2RequestBody(code))
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.ACCEPT_ENCODING, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .body(OAuth2TokensResponseDto.class);
    }

    private OAuth2TokensRequestDto buildOAuth2RequestBody(String code) {
        return OAuth2TokensRequestDto.builder()
            .clientId(CLIENT_ID)
            .clientSecret(CLIENT_SECRET)
            .code(code)
            .redirectUri(this.buildApplicationRedirectUrl())
            .build();
    }

    @Override
    public OAuth2UserInfo getUserInfoFromResourceServer(String accessToken) {
        RestClient restClient = RestClient.create();

        GitHubOAuth2UserInfoDto userInfo = restClient.get()
            .uri(GITHUB_API_USER_INFO_BASE_URL + "/user")
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.ACCEPT_ENCODING, MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .body(GitHubOAuth2UserInfoDto.class);

        List<GitHubOAuth2EmailResponseDto> userEmails = restClient.get()
            .uri(GITHUB_API_USER_INFO_BASE_URL + "/user/emails")
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.ACCEPT_ENCODING, MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .body(new ParameterizedTypeReference<>() {});

        if (userEmails != null) {
            userEmails.stream().filter(email -> email.getPrimary() && email.getVerified())
                .map(GitHubOAuth2EmailResponseDto::getEmail)
                .findFirst()
                .ifPresent(email -> userInfo.setEmail(email));
        }

        return userInfo;
    }
}
