package com.movieworld.movieboard.Config;

import com.movieworld.movieboard.ExceptionHandler.LoginExceptionHandler;
import com.movieworld.movieboard.Filter.LoginLimitFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public LoginExceptionHandler loginExceptionHandler(){
        return new LoginExceptionHandler();
    }

    @Bean
    public BCryptPasswordEncoder encodePwd(){return new BCryptPasswordEncoder();}

    @Bean
    public WebSecurityCustomizer configure(){
        return (web)->web.ignoring().requestMatchers(
          ""
        );
    }

    @Bean
    public LoginLimitFilter loginLimitFilter(){return new LoginLimitFilter();}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf().disable()

                //.addFilterBefore(loginLimitFilter(), UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests()
                .requestMatchers("/mainpage").permitAll()
                .requestMatchers("/board").hasAnyRole("USER")
                .requestMatchers("/mypage").hasAnyRole("USER")
                .anyRequest().permitAll()

                .and()
                .formLogin()
                .loginPage("/newMember")
                .loginProcessingUrl("/LoginForm") //form url
                .failureHandler(loginExceptionHandler());

/*                .and()
                .oauth2Login()
                .loginPage("/LoginForm")
                .userInfoEndpoint();*/

        return http.build();
    }


}
