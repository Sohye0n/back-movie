package com.movieworld.movieboard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@SequenceGenerator(
        name="BoardID_Sequence_Generator",
        sequenceName = "BoardID_Sequence",
        initialValue = 1,
        allocationSize = 1
)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "BoardID_Sequence")
    @NotNull
    @Column(name="ID")
    private Long id;

    @Column(name="WRITERNAME")
    private String WriterName;

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
    private boolean isPrivate;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="node")
    private List<Node> nodes=new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="edge")
    private List<Edge> edges=new ArrayList<>();

    public Board(String title, String content, boolean isprivate) {
        this.title=title;
        this.content=content;
        this.isPrivate=isprivate;
        this.createdAt=LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
        this.views=0;
    }
}
