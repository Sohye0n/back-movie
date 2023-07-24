package com.movieworld.movieboard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class Node {
    @Id
    @NotNull
    @Column(name="ID")
    private String id;

    @ManyToOne
    @JoinColumn(name="Board_ID")
    private Board Board;

    @Column(name="IS_HUB")
    private boolean IsHub;

    @Column(name="PHOTOURL",length=500)
    private String PhotoUrl;

    @Column(name="AUTHORID",length=30)
    private String AuthorID;

    @Column(name="NAME",length=40)
    private String Name;
    @Column(name="DETAILS",length=500)
    private String Details;

    public Node(@NotNull String id, Board board, boolean isHub, String photoUrl, String authorID, String name, String details) {
        this.id = id;
        this.Board =board;
        this.IsHub = isHub;
        this.PhotoUrl = photoUrl;
        this.AuthorID = authorID;
        this.Name = name;
        this.Details = details;
    }

    public void update(boolean isHub, String photoUrl, String authorID, String name, String details){
        this.IsHub =isHub;
        this.PhotoUrl=photoUrl;
        this.AuthorID=authorID;
        this.Name =name;
        this.Details =details;
    }
}