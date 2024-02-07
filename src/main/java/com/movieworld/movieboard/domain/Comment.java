package com.movieworld.movieboard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long commentId;

    @Column(name="cnt")
    public Long cnt;

    @ManyToOne
    @JoinColumn(name="BoardId")
    public Board board;

    @ManyToOne
    @JoinColumn(name="WriterId")
    public Member writer;

    @Column(name="Content")
    public String content;

    @Column(name="CreatedAt")
    public LocalDateTime createdAt;

    @Column(name="isDeleted")
    public Boolean isDeleted;

    @OneToMany(mappedBy = "rootComment")
    private List<ReComment> reComments;

    public Comment(Long cnt, Board board, Member writer, String content, LocalDateTime createdAt, Boolean isDeleted){
        this.cnt=cnt;
        this.board=board;
        this.writer=writer;
        this.content=content;
        this.createdAt=createdAt;
        this.isDeleted=isDeleted;
    }
}
