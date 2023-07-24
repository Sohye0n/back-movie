package com.movieworld.movieboard.DTO;

import com.movieworld.movieboard.domain.Board;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeDTO {
    private int type;
    private String id;
    private Board board;
    private boolean isHub;
    private String PhotoUrl;
    private String AuthorID;
    private String name;
    private String details;
    public NodeDTO(){
    }


    public NodeDTO(int type, String id,boolean isHub, String PhotoUrl, String name, String details){
        this.type=type;
        this.id=id;
        this.isHub=isHub;
        this.PhotoUrl=PhotoUrl;
        this.name=name;
        this.details=details;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isHub() {
        return isHub;
    }

    public void setHub(boolean hub) {
        isHub = hub;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
