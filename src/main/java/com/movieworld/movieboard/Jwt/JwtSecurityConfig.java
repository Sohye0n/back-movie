package com.movieworld.movieboard.Jwt;

import com.movieworld.movieboard.Service.TokenRefreshService;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private TokenProvider tokenProvider;
    private TokenRefreshService tokenRefreshService;
    public JwtSecurityConfig(TokenProvider tokenProvider, TokenRefreshService tokenRefreshService){
        this.tokenProvider=tokenProvider;
        this.tokenRefreshService=tokenRefreshService;
    }

    @Override
    public void configure(HttpSecurity httpSecurity){
        JwtFilter customFilter=new JwtFilter(tokenProvider, tokenRefreshService);
        httpSecurity.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
