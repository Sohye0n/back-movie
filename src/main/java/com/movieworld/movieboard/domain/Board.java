package com.movieworld.movieboard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
//@IdClass(BoardId.class)
@Getter
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name="BoardID")
    public Long BoardId;

    @Column(name="TMDBId")
    private Long tmdbId;

    @ManyToOne
    @JoinColumn(name="writer")
    private Member writer;

    @Column(name="CREATEDAT")
    private LocalDateTime createdAt;

    @Column(name="UPDATEDAT")
    private LocalDateTime updatedAt;

    @Column(name="TITLE")
    private String title;

    @Column(name="CONTENT")
    private String content;

    @Column(name="VIEWS")
    private int views;

    @Column(name="ISPRIVATE")
    private Boolean isPrivate;

    @Column(name="ISTV")
    private Boolean isTv;

    @OneToMany(mappedBy = "Board")
    private List<Node> nodes=new ArrayList<>();

    @OneToMany(mappedBy = "Board")
    private List<Edge> edges=new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<Comment> comments=new ArrayList<>();

    public Board(Long tmdbid, Member writer, String title, LocalDateTime now, LocalDateTime localDateTime, String content, String boardDTOContent, int i, boolean isprivate, Boolean isTv) {
        this.createdAt=LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
        this.tmdbId =tmdbid;
        this.title=title;
        this.content=content;
        this.isPrivate=isprivate;
        this.views=0;
    }

}
