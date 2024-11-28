package com.ukma.competition.platform.config;

import com.ukma.competition.platform.shared.caching.CustomCacheManager;
import com.ukma.competition.platform.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAspectJAutoProxy
@EnableScheduling
@EnableCaching
@EnableSpringDataWebSupport
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CacheManager cacheManager() {
        return new CustomCacheManager();
    }

    @Autowired
    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return email -> userService.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with such email not found"));
    }
}
