package com.movieworld.movieboard.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

@Entity

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long Id;

    private String nickname;

    private String pw;


    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="board")
    private List<Board> boards=new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name="member_authority",
            joinColumns = {@JoinColumn(name="member_id",referencedColumnName = "member_id")},
            inverseJoinColumns = {@JoinColumn(name="authority_name",referencedColumnName = "authority_name")})
    private Set<Authority> authorities;

    public Member(String subject, String s, Collection<? extends GrantedAuthority> authorities) {
    }

    @Builder
    public Member(String id, String pw) {
        this.nickname =id;
        this.pw=pw;
    }

}
