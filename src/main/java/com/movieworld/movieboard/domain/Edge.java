package com.movieworld.movieboard.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@IdClass(EdgeId.class)
public class Edge{
    @Id
    @NotNull
    @Column(name="EdgeId")
    public Long EdgeId;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="BoardID")
    private Board Board;
    private Long ToNode;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumns({
//            @JoinColumn(name = "Edge_id_From"),
//            @JoinColumn(name = "Board_id_From")
//    })
    private Long FromNode;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumns({
//            @JoinColumn(name = "Edge_id_To"),
//            @JoinColumn(name = "Board_id_To")
//    })

    private String Details;

    @Builder
    public Edge(Long id, Long from, Long to, String details, Board board){
        EdgeId=id;
        FromNode=from;
        ToNode=to;
        Details=details;
        Board=board;
    }

    public void updateDetail(String details){
        this.Details =details;
    }

}

