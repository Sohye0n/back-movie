package com.movieworld.movieboard.Config;

import com.movieworld.movieboard.ExceptionHandler.LoginExceptionHandler;
import com.movieworld.movieboard.Jwt.JwtAccessDeniedHandler;
import com.movieworld.movieboard.Jwt.JwtAuthenticationEntryPoint;
import com.movieworld.movieboard.Jwt.JwtSecurityConfig;
import com.movieworld.movieboard.Jwt.TokenProvider;
import com.movieworld.movieboard.Login.CustomAuthenticationManager;
import com.movieworld.movieboard.Login.LoginConfig;
import com.movieworld.movieboard.Login.LoginLimitFilter;
import com.movieworld.movieboard.Repository.RefreshTokenRepository;
import com.movieworld.movieboard.Service.TokenRefreshService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.*;

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
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenRefreshService tokenRefreshService;

    private static final ClearSiteDataHeaderWriter.Directive[] SOURCE = {CACHE, COOKIES, STORAGE, EXECUTION_CONTEXTS};

    public SecurityConfig(TokenProvider tokenProvider, CustomAuthenticationManager customAuthenticationManager, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler, RefreshTokenRepository refreshTokenRepository, TokenRefreshService tokenRefreshService) {
        this.tokenProvider = tokenProvider;
        this.customAuthenticationManager = customAuthenticationManager;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenRefreshService = tokenRefreshService;
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
                //.requestMatchers("/board/view/**").hasAnyRole("USER")
                //.requestMatchers("/comment/**").hasAnyRole("USER")
                .requestMatchers("/newBoard").hasAnyRole("USER")
                .requestMatchers("/network/**").hasAnyRole("USER")
                .anyRequest().permitAll()

                .and()
                .apply(new LoginConfig(tokenProvider,customAuthenticationManager,refreshTokenRepository))

                .and()
                .apply(new JwtSecurityConfig(tokenProvider,tokenRefreshService))

                .and()
                .logout((logout)-> {
                            logout.addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(SOURCE)));
                            logout.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
                        }
                );

        return http.build();
    }

    @Bean
    public LoginLimitFilter loginLimitFilter(){return new LoginLimitFilter();}


}
