package com.movieworld.movieboard.Login;

import com.movieworld.movieboard.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {

    @Autowired
    private final CustomUserDetailsService customUserDetailsService;

    public CustomAuthenticationManager(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException{
        //id에 해당하는 회원이 존재하는지 db에서 확인
        UserDetails userDetails=customUserDetailsService.loadUserByUsername(authentication.getName());
        //비밀번호 체크 로직 추가
        //권한을 준 authentication 리턴
        System.out.println(userDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(),userDetails.getPassword(),userDetails.getAuthorities());
    }
}
