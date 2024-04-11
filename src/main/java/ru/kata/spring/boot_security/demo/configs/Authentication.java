package ru.kata.spring.boot_security.demo.configs;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Collections;

@Component
public class Authentication implements AuthenticationProvider {

    private final UserService userService;

    public Authentication(UserService userService) {
        this.userService = userService;
    }

    @Override
    public org.springframework.security.core.Authentication authenticate(org.springframework.security.core.Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        UserDetails userDetails = userService.loadUserByUsername(userName);
        String password = authentication.getCredentials().toString();

        if (!password.equals(userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}