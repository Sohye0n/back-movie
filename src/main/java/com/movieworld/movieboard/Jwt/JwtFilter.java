package com.movieworld.movieboard.Jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;


public class JwtFilter extends GenericFilterBean {
    private static final Logger logger= LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER="Authorization";
    private TokenProvider tokenProvider;
    public JwtFilter(TokenProvider tokenProvider){
        this.tokenProvider=tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest=(HttpServletRequest) servletRequest;
        String jwt=ResolveToken(httpServletRequest);
        String requestURI=httpServletRequest.getRequestURI();
        logger.debug("JwtFilter-doFilter");
        System.out.println("JwtFilter-doFilter");
        //유효한 토큰이면 권한을 줌
        if(StringUtils.hasText(jwt)&& tokenProvider.validateToken(jwt)){
            //jwt 토큰에 기록되어 있던 사용자의 권한 정보를 가져옴
            Authentication authentication= tokenProvider.getAuthentication(jwt);
            //security context에 이 사용자의 정보를 저장함
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("JwtFilter-doFilter : SecurityContext에 '{}' 인증 정보를 저장했음. url: {}",authentication.getName(),requestURI);
            System.out.println("JwtFilter-doFilter : SecurityContext에 인증 정보를 저장했음. url:"+authentication.getName()+requestURI);
            System.out.println("이 사용자의 정보:"+authentication.getName()+authentication.getAuthorities());
        }
        else{
            logger.debug("JwtFilter-doFilter : 유효한 Jwt 토큰이 없음. url: {}",requestURI);
            System.out.println("JwtFilter-doFilter : no JWT token. url: {}"+requestURI);
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    private String ResolveToken(HttpServletRequest httpServletRequest) {
        logger.debug("JwtFilter-ResolveToken");
        String bearerToken= httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        //jwt 토큰은 bearer로 시작함
        if(StringUtils.hasText(bearerToken)&&bearerToken.startsWith("Bearer ")){
            logger.debug("JwtFilter-ResolveToken:succeed");
            //bearer 뒤부터 유의미한 정보이기 때문에
            return bearerToken.substring(7);
        }
        return null;
    }
}
