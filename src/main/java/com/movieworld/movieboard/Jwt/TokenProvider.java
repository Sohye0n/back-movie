package com.movieworld.movieboard.Jwt;

import com.movieworld.movieboard.domain.Member;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

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
    private Key key;

    public TokenProvider(@Value("${jwt.secret}")String secret, @Value("${jwt.token-validity-in-seconds}")long tokenValidityInMilliseconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds;
    }


    //다른 속성들이 다 설정되고 객체가 생성된 후 실행됨
    @Override
    public void afterPropertiesSet(){
        logger.debug("TokenProvider-afterPropertiesSet");
        System.out.println("TokenProvider-afterPropertiesSet");
        byte[] KeyBytes= Base64.getDecoder().decode(secret);
        this.key=Keys.hmacShaKeyFor(KeyBytes);
    }

    //
    public String createToken(Authentication authentication){
      logger.debug("TokenProvider-createToken");
      System.out.println("TokenProvider-createToken");
      String authorities=authentication.getAuthorities().stream()
              .map(GrantedAuthority::getAuthority)//GrantedAuthority 클래스 내의 getAuthority를 호출하여 이를 스트링 타입으로 변환
              .collect(Collectors.joining(",")); //얻은 스트링들을 ,로 연결한 후 반환
      long now=(new Date()).getTime();
      Date validity=new Date(now+this.tokenValidityInMilliseconds);

      //jwt의 페이로드에 담기는 내용
      //claim: 사용자 권한 정보와 데이터를 일컫는 말
      return Jwts.builder()
              .setSubject(authentication.getName()) //토큰 제목
              .claim(AUTHORITIES_KEY,authorities) //토큰에 담길 내용
              .signWith(key, SignatureAlgorithm.HS512)
              .setExpiration(validity)
              .compact();
    }

    //유효한 jwt 토큰을 가지고 있는 자에게 권한을 줌.
    public Authentication getAuthentication(String token){
        logger.debug("TokenProvider-getAuthentication");
        System.out.println("TokenProvider-getAuthentication");
        Claims claims=Jwts
                .parserBuilder() //받은 token을 파싱할 수 있는 객체(JwtParserBuilder)를 리턴
                .setSigningKey(key) //ParserBuilder의 key 설정
                .build() //ParserBuilder을 통해 Parser 리턴.
                .parseClaimsJws(token) //토큰을 파싱하여
                .getBody(); //body를 리턴함

        Collection<? extends GrantedAuthority> authorities= Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        //이러면 기존 멤버도 새로 로그인할 때마다 DB에 추가되는 듯..? 확인해보고 고쳐야.
        Member principal=new Member(claims.getSubject(),"", authorities);
        System.out.println(claims.getSubject());
        //일반적인 세션 기반의 id/pw 인증방식이었다면 token에 pw가 담겼을 것...?
        //jwt 로그인에서는 발급받은 jwt 토큰인 token을 기반으로 인증하기 때문에 credential에 token이 담김
        //얘는 인증받은 토큰을 리턴함.
        return new UsernamePasswordAuthenticationToken(principal,token,authorities);
    }

    public boolean validateToken(String token){
        logger.debug("TokenProvider-validateToken");
        System.out.println("TokenProvider-validateToken");
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch(io.jsonwebtoken.security.SecurityException|MalformedJwtException e){
            logger.info("잘못된 JWT 서명입니다.");
        } catch(ExpiredJwtException e){
            logger.info("만료된 JWT 토큰입니다");
        }catch(UnsupportedJwtException e){
            logger.info("지원되지 않는 JWT 토큰입니다");
        }catch(IllegalArgumentException e){
            logger.info("JWT 토큰이 잘못되었습니다");
        }
        return false;
    }
}
