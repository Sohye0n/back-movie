package com.movieworld.movieboard.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {
    private String Nickname;
    private String pw;
    private String Jwt;

    public MemberDTO(String jwt) {

    }
}
