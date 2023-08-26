package com.movieworld.movieboard.Login;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieworld.movieboard.Jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final TokenProvider tokenProvider;
    private final CustomAuthenticationManager customAuthenticationManager;

    public LoginFilter(TokenProvider tokenProvider, CustomAuthenticationManager customAuthenticationManager) {
        this.tokenProvider = tokenProvider;
        this.customAuthenticationManager = customAuthenticationManager;
    }

    //UsernamePasswordToken을 생성 후 Authentication Manager에게 이를 넘김
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException{
        try (InputStream is=request.getInputStream()){
            ObjectMapper objectMapper=new ObjectMapper();
            JsonNode jsonNode=objectMapper.readTree(is);

            String username=jsonNode.get("nickname").asText();
            String password=jsonNode.get("pw").asText();
            System.out.println("username"+username);
            UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
            // Allow subclasses to set the "details" property
            setDetails(request, authRequest);
            System.out.println("login filter running...");

            //authentication manager에게 토큰을 넘겨줌
            Authentication authentication=customAuthenticationManager.authenticate(authRequest);
            return authentication;
        } catch (IOException e) {
            throw new AuthenticationServiceException("Failed to parse JSON authentication request");
        }
    }

    //인증 성공 후
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        logger.info("authentication success");
        System.out.println("authentication success");
        String jwt=tokenProvider.createToken(authResult);

        //String jsonBody=new ObjectMapper().writeValueAsString(jwt);
        response.setHeader("Authorization","Bearer"+jwt);
        chain.doFilter(request,response);
    }

    //인증 실패 시
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        logger.info("authentication failed");
        System.out.println("authentication failed");
        response.setStatus(401);
    }

}
