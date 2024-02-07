package com.movieworld.movieboard.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class ReComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long commentId;

    @ManyToOne
    @JoinColumn(name="ref")
    public Comment rootComment;

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

    public ReComment(Comment rootComment, Long cnt, Board board, Member writer, String content, LocalDateTime createdAt, Boolean isDeleted){
        this.rootComment=rootComment;
        this.cnt=cnt;
        this.board=board;
        this.writer=writer;
        this.content=content;
        this.createdAt=createdAt;
        this.isDeleted=isDeleted;
    }
}
