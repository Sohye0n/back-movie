package com.movieworld.movieboard.Jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    //private static final Logger logger= LoggerFactory.getLogger(JwtFilter.class);
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
        //logger.debug("JwtAutenticationEntryPoint");
        Integer exception= (Integer) request.getAttribute("exception");
        System.out.println("JWTAuthenticationEntryPoint"+exception);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        if (exception.equals(JwtExceptionCode.ILLEGAL_TOKEN.getCode())) {
            response.getWriter().write(JwtExceptionCode.ILLEGAL_TOKEN.getMessage());
        } else if (exception.equals(JwtExceptionCode.UNSUPPORTED_TOKEN.getCode())) {
            response.getWriter().write(JwtExceptionCode.UNSUPPORTED_TOKEN.getMessage());
        } else if (exception.equals(JwtExceptionCode.EXPIRED_TOKEN.getCode())) {
            System.out.println("here");
            response.getWriter().write(JwtExceptionCode.EXPIRED_TOKEN.getMessage());
        } else if (exception.equals(JwtExceptionCode.WRONG_TOKEN.getCode())) {
            response.getWriter().write(JwtExceptionCode.WRONG_TOKEN.getMessage());
        }
    }
}
