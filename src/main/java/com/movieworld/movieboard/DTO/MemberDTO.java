package com.movieworld.movieboard.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberDTO {
    private String nickname;
    private String pw;
    private String Jwt;

    public MemberDTO(){

    }
    public MemberDTO(String nickname, String pw) {
        this.nickname =nickname;
        this.pw=pw;
    }
}
