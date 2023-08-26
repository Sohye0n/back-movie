package com.movieworld.movieboard.Config;

import com.movieworld.movieboard.ExceptionHandler.LoginExceptionHandler;
import com.movieworld.movieboard.Jwt.JwtAccessDeniedHandler;
import com.movieworld.movieboard.Jwt.JwtAuthenticationEntryPoint;
import com.movieworld.movieboard.Jwt.JwtSecurityConfig;
import com.movieworld.movieboard.Jwt.TokenProvider;
import com.movieworld.movieboard.Login.CustomAuthenticationManager;
import com.movieworld.movieboard.Login.LoginLimitConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public LoginExceptionHandler loginExceptionHandler(){
        return new LoginExceptionHandler();
    }

    @Bean
    public WebSecurityCustomizer configure(){
        return (web)->web.ignoring().requestMatchers(
          "/css/**","/js/**","/images/**","/favicon.ico","/error");
    }

    private final TokenProvider tokenProvider;
    private final CustomAuthenticationManager customAuthenticationManager;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    public SecurityConfig(TokenProvider tokenProvider, CustomAuthenticationManager customAuthenticationManager, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.tokenProvider = tokenProvider;
        this.customAuthenticationManager = customAuthenticationManager;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain1(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/login").permitAll()
                .anyRequest().permitAll()
                .and()
                .apply(new LoginLimitConfig(tokenProvider,customAuthenticationManager));
        return http.build();
    }


    @Bean
    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeHttpRequests()
                .requestMatchers("/board/view/**").hasAnyRole("USER")
                .requestMatchers("/newBoard").hasAnyRole("USER")
                .requestMatchers("/network/**").hasAnyRole("USER")
                .anyRequest().permitAll()

                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }


}
