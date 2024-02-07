package com.movieworld.movieboard.Login;

import com.movieworld.movieboard.Jwt.TokenProvider;
import com.movieworld.movieboard.Repository.RefreshTokenRepository;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class LoginConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private TokenProvider tokenProvider;
    private CustomAuthenticationManager customAuthenticationManager;
    private RefreshTokenRepository refreshTokenRepository;
    public LoginConfig(TokenProvider tokenProvider, CustomAuthenticationManager customAuthenticationManager, RefreshTokenRepository refreshTokenRepository){
        this.tokenProvider=tokenProvider;
        this.customAuthenticationManager=customAuthenticationManager;
        this.refreshTokenRepository=refreshTokenRepository;
    }

    @Override
    public void configure(HttpSecurity httpSecurity){
        LoginFilter customFilter=new LoginFilter(tokenProvider,customAuthenticationManager, refreshTokenRepository);
        httpSecurity.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
