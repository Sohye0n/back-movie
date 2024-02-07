package com.movieworld.movieboard.Jwt;

import com.movieworld.movieboard.Service.TokenRefreshService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JwtFilter extends OncePerRequestFilter {
    private static final Logger logger= LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER="Authorization";
    private TokenProvider tokenProvider;
    private final TokenRefreshService tokenRefreshService;
    public JwtFilter(TokenProvider tokenProvider, TokenRefreshService tokenRefreshService){
        this.tokenProvider=tokenProvider;
        this.tokenRefreshService = tokenRefreshService;
    }

    private String ResolveAccessToken(HttpServletRequest httpServletRequest) {
        String bearerToken= httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        //jwt 토큰은 bearer로 시작함
        if(StringUtils.hasText(bearerToken)&&bearerToken.startsWith("Bearer ")){
            logger.info("JwtFilter-ResolveToken:succeed");
            //bearer 뒤부터 유의미한 정보이기 때문에
            return bearerToken.substring(7);
        }
        logger.info("failed..");
        return null;
    }

    private String ResolveRefreshToken(HttpServletRequest httpServletRequest) {
        logger.debug("JwtFilter-ResolveRefreshToken");
        Cookie[] cookies=httpServletRequest.getCookies();
        String resolveToken=null;
        if(cookies!=null){
            for(Cookie cookie : cookies){
                logger.info("cookie name : "+cookie.getName());
                logger.info("cookie value : "+cookie.getValue());
                if(cookie.getName().equals("refreshToken")) resolveToken=cookie.getValue();
            }
        }
        logger.info(resolveToken);
        return resolveToken;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken=ResolveAccessToken(request);
        String refreshToken=ResolveRefreshToken(request);
        String requestURI=request.getRequestURI();
        logger.info(accessToken);
        logger.debug("JwtFilter-doFilter");

        if(StringUtils.hasText(refreshToken)) logger.info("pass1");
        logger.info(requestURI);

        //refresh accessToken
        if(StringUtils.hasText(refreshToken) && requestURI.equals("/refresh")){
            logger.info("refreshing");
            //1. refreshToken의 유효성 검사 - refreshToken에서 userid를 가져와 이에 해당하는 값을 찾음
            if(tokenProvider.validateRefreshToken(refreshToken,request) && tokenProvider.checkRefreshToken(refreshToken)){
                //2. refreshToken을 사용하여 authentication 객체 생성
                Authentication authentication= tokenProvider.getAuthentication(refreshToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                //3. 새로운 accessToken 발급받음
                String AccessJwt=tokenProvider.createAccessToken(authentication);
                response.setHeader("Authorization","Bearer"+AccessJwt);
                logger.info(AccessJwt);

                //4. refreshToken의 기한이 얼마 남지 않았을 경우 재발급
                Long expirationTime=tokenProvider.getExpirationTime(refreshToken);
                Long currentTime=System.currentTimeMillis();

                //1분 안에 만료될 예정이라면 재발급한다.
                if(expirationTime-currentTime<1*60*1000){
                    logger.info("refreshToken is about to expire");
                    String refreshJwt=tokenProvider.createRefreshToken(authentication);
                    response.setHeader("Set-Cookie",
                            "refreshToken=" + refreshJwt + "; " +
                                    "Path=/;" +
                                    "Domain=localhost; " +
                                    "HttpOnly; " +
                                    "Max-Age=604800; "
                    );

                    //5. refreshToken 저장
                    tokenRefreshService.updateRefreshToken(refreshToken,refreshJwt,authentication.getName());
                }

                logger.info("refreshing ended");
            }
        }


        //유효한 토큰이면 권한을 줌
        else if(StringUtils.hasText(accessToken)&& tokenProvider.validateAccessToken(accessToken,request)){
            //jwt 토큰에 기록되어 있던 사용자의 권한 정보를 가져옴
            Authentication authentication= tokenProvider.getAuthentication(accessToken);
            //security context에 이 사용자의 정보를 저장함
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("JwtFilter-doFilter : SecurityContext에 '{}' 인증 정보를 저장했음. url: {}",authentication.getName(),requestURI);
        }

        else{
            logger.info("JwtFilter-doFilter : 유효한 Jwt 토큰이 없음. url: {}",requestURI);
        }
        filterChain.doFilter(request,response);
    }
}
