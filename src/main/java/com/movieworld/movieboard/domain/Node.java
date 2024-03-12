package com.movieworld.movieboard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Entity
@Getter
@IdClass(NodeId.class)
@NoArgsConstructor
public class Node{
    @Id
    @NotNull
    @Column(name="NodeId")
    public Long NodeId;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="BoardID")
    private Board Board;

    @Column(name="IS_HUB")
    private Boolean IsDeleted;

    @Column(name="PHOTOURL",length=500)
    private String PhotoUrl;

    @Column(name="NAME",length=40)
    private String Name;
    @Column(name="DETAILS",length=500)
    private String Details;


    public Node(@NotNull Long id, Board board, boolean isDeleted, String photoUrl, String name, String details) {
        this.NodeId = id;
        this.Board =board;
        this.IsDeleted = isDeleted;
        this.PhotoUrl = photoUrl;
        this.Name = name;
        this.Details = details;
    }

    public void updateName(String name){
        this.Name =name;
    }

    public void updateDetail(String details){
        this.Details =details;
    }
}

