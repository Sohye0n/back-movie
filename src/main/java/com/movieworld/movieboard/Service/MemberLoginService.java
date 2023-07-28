package com.movieworld.movieboard.Service;

import com.movieworld.movieboard.DTO.MemberDTO;
import com.movieworld.movieboard.Repository.MemberRepository;
import com.movieworld.movieboard.domain.Board;
import com.movieworld.movieboard.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class MemberLoginService {

    private final MemberRepository memberRepository;

    public MemberLoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public boolean Login(MemberDTO memberDTO) throws Exception {
        Member member=memberRepository.findById(memberDTO.getID()).orElseThrow(()->new Exception());
        if(member.getPw()==memberDTO.getPw()) return true;
        else return false;
    }


}
