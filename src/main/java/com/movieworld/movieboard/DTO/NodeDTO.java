package com.movieworld.movieboard.DTO;

import com.movieworld.movieboard.domain.Board;
import com.movieworld.movieboard.domain.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeDTO {
    private Long id;
    private Long board;
    private boolean isDeleted;
    private String photoUrl;
    private String name;
    private String details;
    private int type;
    public NodeDTO(){
    }

    public NodeDTO(Long id, Boolean isDeleted, String photoUrl, String name, String details){
        this.id=id;
        this.board= Long.valueOf(-1);
        this.isDeleted=isDeleted;
        this.photoUrl=photoUrl;
        this.name=name;
        this.details=details;
        this.board=null;
        this.type=-1;
    }

    public NodeDTO(int type, Long id,boolean isDeleted, String photoUrl, String name, String details){
        this.type=type;
        this.id=id;
        this.isDeleted=isDeleted;
        this.photoUrl =photoUrl;
        this.name=name;
        this.details=details;
    }

}
