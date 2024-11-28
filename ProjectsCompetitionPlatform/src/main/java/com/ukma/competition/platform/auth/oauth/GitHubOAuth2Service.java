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

    String githubApiTokenUrl;
    String githubApiUserInfoBaseUrl;

    public GitHubOAuth2Service(
        @Value("${oauth2.provider.github.url.apis.token}") String githubApiTokenBaseUrl,
        @Value("${oauth2.provider.github.url.apis.user-info}") String githubApiUserInfoBaseUrl,
        @Value("${oauth2.provider.github.url.authPage}") String externalAuthPage,
        @Value("${oauth2.provider.github.client.id}") String clientId,
        @Value("${oauth2.provider.github.client.secret}") String clientSecret,
        @Value("${oauth2.state}") String state,
        @Value("${oauth2.provider.github.scope}") String scope,
        UserService userService,
        JwtService jwtService
    ) {
        super(
            externalAuthPage,
            clientId,
            clientSecret,
            state,
            scope,
            userService,
            jwtService
        );
        this.githubApiTokenUrl = githubApiTokenBaseUrl;
        this.githubApiUserInfoBaseUrl = githubApiUserInfoBaseUrl;
    }

    @Override
    public AuthenticationProvider getOAuth2AuthenticationProvider() {
        return AuthenticationProvider.GITHUB;
    }

    @Override
    public OAuth2TokensResponseDto requestOAuth2Tokens(String code) {
        RestClient restClient = RestClient.create();

         return restClient.post()
            .uri(githubApiTokenUrl)
            .body(buildOAuth2RequestBody(code))
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.ACCEPT_ENCODING, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .body(OAuth2TokensResponseDto.class);
    }

    private OAuth2TokensRequestDto buildOAuth2RequestBody(String code) {
        return OAuth2TokensRequestDto.builder()
            .clientId(clientId)
            .clientSecret(clientSecret)
            .code(code)
            .redirectUri(this.buildApplicationRedirectUrl())
            .build();
    }

    @Override
    public OAuth2UserInfo getUserInfoFromResourceServer(String accessToken) {
        RestClient restClient = RestClient.create();

        GitHubOAuth2UserInfoDto userInfo = restClient.get()
            .uri(githubApiUserInfoBaseUrl + "/user")
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.ACCEPT_ENCODING, MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .body(GitHubOAuth2UserInfoDto.class);

        List<GitHubOAuth2EmailResponseDto> userEmails = restClient.get()
            .uri(githubApiUserInfoBaseUrl + "/user/emails")
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.ACCEPT_ENCODING, MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .body(new ParameterizedTypeReference<>() {});

        if (userEmails != null) {
            assert userInfo != null;
            userEmails.stream().filter(email -> email.getPrimary() && email.getVerified())
                .map(GitHubOAuth2EmailResponseDto::getEmail)
                .findFirst()
                .ifPresent(userInfo::setEmail);
        }

        return userInfo;
    }
}
