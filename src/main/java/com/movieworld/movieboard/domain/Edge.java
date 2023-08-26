package com.movieworld.movieboard.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Entity
@Getter
public class Edge {
    @Id
    @NotNull
    private Long Id;

    @OneToOne
    private Node From;
    @OneToOne
    private Node To;

    @ManyToOne
    private Board board;

    @Builder
    public Edge(Node from, Node to){
        this.From=from;
        this.To=to;
    }

}
