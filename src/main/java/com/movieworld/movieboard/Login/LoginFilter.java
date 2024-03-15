package com.movieworld.movieboard.Login;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieworld.movieboard.Jwt.TokenProvider;
import com.movieworld.movieboard.Repository.RefreshTokenRepository;
import com.movieworld.movieboard.domain.RefreshToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.InputStream;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final TokenProvider tokenProvider;
    private final CustomAuthenticationManager customAuthenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginFilter(TokenProvider tokenProvider, CustomAuthenticationManager customAuthenticationManager, RefreshTokenRepository refreshTokenRepository) {
        this.tokenProvider = tokenProvider;
        this.customAuthenticationManager = customAuthenticationManager;
        this.refreshTokenRepository = refreshTokenRepository;
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

            UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
            // Allow subclasses to set the "details" property
            setDetails(request, authRequest);

            //authentication manager에게 토큰을 넘겨줌
            Authentication authentication=customAuthenticationManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
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

        //Access Token
        String AccessJwt=tokenProvider.createAccessToken(authResult);
        response.setHeader("Authorization","Bearer "+AccessJwt);

        //Refresh Token
        String RefreshJwt=tokenProvider.createRefreshToken(authResult);
        response.setHeader("Set-Cookie",
                "refreshToken=" + RefreshJwt + "; " +
                        "Path=/;" +
                        "Domain=localhost; " +
                        "HttpOnly; " +
                        "Max-Age=604800; "
        );

        //get username
        String username=authResult.getName();
        //save refreshToken
        RefreshToken refreshToken=new RefreshToken(RefreshJwt,username);
        refreshTokenRepository.save(refreshToken);

        chain.doFilter(request,response);
    }

    //인증 실패 시
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        logger.info("authentication failed");
        response.setStatus(401);
    }

}
