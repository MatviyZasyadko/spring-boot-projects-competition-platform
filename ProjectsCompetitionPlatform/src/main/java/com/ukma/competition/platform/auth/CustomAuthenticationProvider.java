package com.ukma.competition.platform.auth;

import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.users.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // method should be present to extend abstract class
    }

    @Override
    protected UserDetails retrieveUser(String username,
                                       UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        GrantedAuthority grantedAuthority = usernamePasswordAuthenticationToken.getAuthorities() != null && !usernamePasswordAuthenticationToken.getAuthorities().isEmpty()
            ? usernamePasswordAuthenticationToken.getAuthorities().stream().findFirst().get()
            : null;
        UserRole userRole = grantedAuthority != null && grantedAuthority.getAuthority().equals(UserRole.ADMIN.getAuthority())
            ? UserRole.ADMIN
            : UserRole.USER;
        return UserEntity.builder().email(username).userRole(userRole).build();
    }


}
