package com.movieworld.movieboard.Config;

import com.movieworld.movieboard.ExceptionHandler.LoginExceptionHandler;
import com.movieworld.movieboard.Filter.LoginLimitFilter;
import com.movieworld.movieboard.Jwt.JwtAccessDeniedHandler;
import com.movieworld.movieboard.Jwt.JwtAuthenticationEntryPoint;
import com.movieworld.movieboard.Jwt.JwtSecurityConfig;
import com.movieworld.movieboard.Jwt.TokenProvider;
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
    public BCryptPasswordEncoder bCryptPasswordEncoder(){return new BCryptPasswordEncoder();}

    @Bean
    public WebSecurityCustomizer configure(){
        return (web)->web.ignoring().requestMatchers(
          "/css/**","/js/**","/images/**","/favicon.ico","/error");
    }

    @Bean
    public LoginLimitFilter loginLimitFilter(){return new LoginLimitFilter();}

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    public SecurityConfig(TokenProvider tokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }


/*    @Bean
    public LoginLimitFilter loginLimitFilter(){return new LoginLimitFilter();}*/

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)


/*                .and()
                .formLogin() // Add formLogin() to handle login form
                .loginPage("/login") // Set the login page URL
                .loginProcessingUrl("/LoginForm") // Set the form submission URL
                .failureHandler(loginExceptionHandler())*/

                .and()
                .authorizeHttpRequests()
                .requestMatchers("/").permitAll()
                .requestMatchers("/mainpage").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/LoginForm").permitAll()
                .requestMatchers("newMember").permitAll()
                .requestMatchers("/join").permitAll()
                .requestMatchers("/JoinForm").permitAll()
                .requestMatchers("/Boardlist").hasAnyRole("ROLE_USER")
                .anyRequest().permitAll()

                .and()
                // 403 예외처리 핸들링
                .exceptionHandling().accessDeniedPage("/login")


                .and()
                .apply(new JwtSecurityConfig(tokenProvider));


        return http.build();
    }


}
