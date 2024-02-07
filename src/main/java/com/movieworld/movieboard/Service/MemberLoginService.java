package com.movieworld.movieboard.Service;

import com.movieworld.movieboard.DTO.MemberDTO;
import com.movieworld.movieboard.Repository.AuthRepository;
import com.movieworld.movieboard.Repository.MemberRepository;
import com.movieworld.movieboard.Repository.RefreshTokenRepository;
import com.movieworld.movieboard.domain.Authority;
import com.movieworld.movieboard.domain.Member;
import com.movieworld.movieboard.domain.RefreshToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class MemberLoginService {

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public MemberLoginService(MemberRepository memberRepository, AuthRepository authRepository, RefreshTokenRepository refreshTokenRepository) {
        this.memberRepository = memberRepository;
        this.authRepository = authRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public boolean check(MemberDTO memberDTO) throws Exception {
        try {
            Member member = memberRepository.findByNickname(memberDTO.getNickname());
            return false;
        }
        catch (Exception exception){
            return true;
        }
    }

    public void join(MemberDTO memberDTO){
        Authority authority=authRepository.findById("ROLE_USER").orElseThrow(()->new IllegalStateException("해당하는 역할이 없음"));
        System.out.println(authority);
        System.out.println(authority);
        Member member= Member.builder()
                .id(memberDTO.getNickname())
                .pw(memberDTO.getPw())
                .authority(authority)
                .build();
        memberRepository.save(member);
    }

    public void saveRefreshToken(HttpServletRequest httpServletRequest, String nickname) {
        Cookie[] cookies=httpServletRequest.getCookies();
        String resolveToken=null;
        if(cookies!=null){
            for(Cookie cookie : cookies){
                System.out.println("cookie name : "+cookie.getName());
                System.out.println("cookie value : "+cookie.getValue());
                if(cookie.getName().equals("refreshToken")) resolveToken=cookie.getValue();
            }
        }
        RefreshToken refreshToken=new RefreshToken(resolveToken,nickname);
        refreshTokenRepository.save(refreshToken);
    }
}
