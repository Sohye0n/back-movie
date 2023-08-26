package com.movieworld.movieboard.Login;

import com.movieworld.movieboard.Jwt.TokenProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class LoginLimitConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private TokenProvider tokenProvider;
    private CustomAuthenticationManager customAuthenticationManager;
    public LoginLimitConfig(TokenProvider tokenProvider,CustomAuthenticationManager customAuthenticationManager){
        this.tokenProvider=tokenProvider;
        this.customAuthenticationManager=customAuthenticationManager;
    }

    @Override
    public void configure(HttpSecurity httpSecurity){
        LoginFilter customFilter=new LoginFilter(tokenProvider,customAuthenticationManager);
        httpSecurity.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
