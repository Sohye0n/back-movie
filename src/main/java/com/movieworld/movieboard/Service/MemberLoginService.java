package com.movieworld.movieboard.Service;

import com.movieworld.movieboard.DTO.MemberDTO;
import com.movieworld.movieboard.Repository.AuthRepository;
import com.movieworld.movieboard.Repository.MemberRepository;
import com.movieworld.movieboard.domain.Authority;
import com.movieworld.movieboard.domain.Member;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberLoginService {

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;

    public MemberLoginService(MemberRepository memberRepository, AuthRepository authRepository) {
        this.memberRepository = memberRepository;
        this.authRepository = authRepository;
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

}
