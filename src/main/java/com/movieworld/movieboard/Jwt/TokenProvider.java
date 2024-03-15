package com.movieworld.movieboard.Jwt;

import com.movieworld.movieboard.Service.TokenRefreshService;
import com.movieworld.movieboard.domain.Member;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.io.IOException;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

    //로그를 찍을 수 있도록 해줌.
    private final Logger logger= LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY="auth";
    private final String secret;
    private final long tokenValidityInMilliseconds;
    private final TokenRefreshService tokenRefreshService;
    private Key key;

    public TokenProvider(@Value("${jwt.secret}")String secret, @Value("${jwt.token-validity-in-seconds}")long tokenValidityInMilliseconds, TokenRefreshService tokenRefreshService) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds;
        this.tokenRefreshService = tokenRefreshService;
    }


    //다른 속성들이 다 설정되고 객체가 생성된 후 실행됨
    @Override
    public void afterPropertiesSet(){
        logger.debug("TokenProvider-afterPropertiesSet");
        byte[] KeyBytes= Base64.getDecoder().decode(secret);
        this.key=Keys.hmacShaKeyFor(KeyBytes);
    }

    //accessToken
    public String createAccessToken(Authentication authentication){
      logger.debug("TokenProvider-createAccessToken");
      String authorities=authentication.getAuthorities().stream()
              .map(GrantedAuthority::getAuthority)//GrantedAuthority 클래스 내의 getAuthority를 호출하여 이를 스트링 타입으로 변환
              .collect(Collectors.joining(",")); //얻은 스트링들을 ,로 연결한 후 반환
      long now=(new Date()).getTime();
      Date validity=new Date(now+5*60*1000); //5min

      //jwt의 페이로드에 담기는 내용
      //claim: 사용자 권한 정보와 데이터를 일컫는 말
      return Jwts.builder()
              .setSubject(authentication.getName()) //토큰 제목
              .claim(AUTHORITIES_KEY,authorities) //토큰에 담길 내용
              .signWith(key, SignatureAlgorithm.HS512)
              .setExpiration(validity)
              .compact();
    }

    //refreshToken
    public String createRefreshToken(Authentication authentication){
        logger.debug("TokenProvider-createRefreshToken");
        String authorities=authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")); //얻은 스트링들을 ,로 연결한 후 반환
        long now=(new Date()).getTime();
        //Date validity=new Date(now+5*this.tokenValidityInMilliseconds); //10min
        Date validity=new Date(now+10*60*1000); //10min
        logger.info(validity.toString());

        //jwt의 페이로드에 담기는 내용
        //claim: 사용자 권한 정보와 데이터를 일컫는 말
        return Jwts.builder()
                .setSubject(authentication.getName()) //토큰 제목
                .claim(AUTHORITIES_KEY,authorities) //토큰에 담길 내용
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    //token을 해석하여 유저 이름을 리턴함.
    public String getUsername(String token){
        Claims claims=Jwts
                .parserBuilder() //받은 token을 파싱할 수 있는 객체(JwtParserBuilder)를 리턴
                .setSigningKey(key) //ParserBuilder의 key 설정
                .build() //ParserBuilder을 통해 Parser 리턴.
                .parseClaimsJws(token) //토큰을 파싱하여
                .getBody(); //body를 리턴함
        return claims.getSubject();
    }

    //유효한 jwt 토큰을 가지고 있는 자에 대한 정보를 가져옴.
    public Authentication getAuthentication(String token){
        logger.debug("TokenProvider-getAuthentication");
        Claims claims=Jwts
                .parserBuilder() //받은 token을 파싱할 수 있는 객체(JwtParserBuilder)를 리턴
                .setSigningKey(key) //ParserBuilder의 key 설정
                .build() //ParserBuilder을 통해 Parser 리턴.
                .parseClaimsJws(token) //토큰을 파싱하여
                .getBody(); //body를 리턴함

        Collection<? extends GrantedAuthority> authorities= Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal=new User(claims.getSubject(),"", authorities);
        return new UsernamePasswordAuthenticationToken(principal,token,authorities);
    }

    public boolean validateAccessToken(String token, HttpServletRequest httpServletRequest) throws IOException {
        logger.debug("TokenProvider-validateToken");
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch(io.jsonwebtoken.security.SecurityException|MalformedJwtException e){
            logger.info("잘못된 JWT 서명입니다.");
            httpServletRequest.setAttribute("exception",JwtExceptionCode.WRONG_TOKEN.getCode());
            System.out.println("1 JWT");
        } catch(ExpiredJwtException e){
            logger.info("2 JWT");
            httpServletRequest.setAttribute("exception",JwtExceptionCode.EXPIRED_TOKEN.getCode());
            System.out.println("만료된 JWT 토큰입니다");
        }catch(UnsupportedJwtException e){
            logger.info("지원되지 않는 JWT 토큰입니다");
            httpServletRequest.setAttribute("exception",JwtExceptionCode.UNSUPPORTED_TOKEN.getCode());
            System.out.println("3 JWT");
        }catch(IllegalArgumentException e){
            logger.info("JWT 토큰이 잘못되었습니다");
            httpServletRequest.setAttribute("exception",JwtExceptionCode.ILLEGAL_TOKEN.getCode());
            System.out.println("4 JWT");
        }
        return false;
    }

    public boolean validateRefreshToken(String token, HttpServletRequest httpServletRequest){
        logger.info("tokenProvider-validateRefreshToken");
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch(io.jsonwebtoken.security.SecurityException|MalformedJwtException e){
            logger.info("잘못된 JWT 서명입니다.");
            httpServletRequest.setAttribute("exception",JwtExceptionCode.WRONG_TOKEN.getCode());
            System.out.println("1 JWT");
        } catch(ExpiredJwtException e){
            logger.info("2 JWT");
            httpServletRequest.setAttribute("exception",JwtExceptionCode.EXPIRED_TOKEN.getCode());
            System.out.println("만료된 JWT 토큰입니다");
        }catch(UnsupportedJwtException e){
            logger.info("지원되지 않는 JWT 토큰입니다");
            httpServletRequest.setAttribute("exception",JwtExceptionCode.UNSUPPORTED_TOKEN.getCode());
            System.out.println("3 JWT");
        }catch(IllegalArgumentException e){
            logger.info("JWT 토큰이 잘못되었습니다");
            httpServletRequest.setAttribute("exception",JwtExceptionCode.ILLEGAL_TOKEN.getCode());
            System.out.println("4 JWT");
        }
        return false;
    }

    public boolean checkRefreshToken(String token){
        logger.info("checkRefreshToken");
        //1. 토큰에서 claims 가져오기
        Claims claims=Jwts
                .parserBuilder() //받은 token을 파싱할 수 있는 객체(JwtParserBuilder)를 리턴
                .setSigningKey(key) //ParserBuilder의 key 설정
                .build() //ParserBuilder을 통해 Parser 리턴.
                .parseClaimsJws(token) //토큰을 파싱하여
                .getBody(); //body를 리턴함

        //2. 유저 email 가져오기
        String subject=claims.getSubject();

        //3. db에서 refreshtoken을 키로 가지는 email 밸류를 가져와 비교
        return tokenRefreshService.match(token,subject);
    }

    public Long getExpirationTime(String token) {

        Claims claims=Jwts
                .parserBuilder() //받은 token을 파싱할 수 있는 객체(JwtParserBuilder)를 리턴
                .setSigningKey(key) //ParserBuilder의 key 설정
                .build() //ParserBuilder을 통해 Parser 리턴.
                .parseClaimsJws(token) //토큰을 파싱하여
                .getBody(); //body를 리턴함

        return claims.getExpiration().getTime();

    }
}
