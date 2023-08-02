package com.movieworld.movieboard.Service;

import com.movieworld.movieboard.DTO.MemberDTO;
import com.movieworld.movieboard.Repository.MemberRepository;
import com.movieworld.movieboard.domain.Member;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MemberLoginService {

    private final MemberRepository memberRepository;

    public MemberLoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public boolean Login(MemberDTO memberDTO) throws Exception {
        Member member=memberRepository.findByNickname(memberDTO.getNickname());
        System.out.println(member.getPw());
        System.out.println(member.getNickname());
        if(Objects.equals(member.getPw(), memberDTO.getPw())) return true;
        else return false;
    }

    public void Join(MemberDTO memberDTO){
        Member member= Member.builder()
                .id(memberDTO.getNickname())
                .pw(memberDTO.getPw())
                .build();
        System.out.println(memberDTO.getNickname());
        System.out.println(memberDTO.getPw());

        memberRepository.save(member);
    }


}
