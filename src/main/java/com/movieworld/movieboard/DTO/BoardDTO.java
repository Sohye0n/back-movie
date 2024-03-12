package com.movieworld.movieboard.DTO;

import com.movieworld.movieboard.domain.EdgeId;
import com.movieworld.movieboard.domain.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BoardDTO {

    public Long id;
    public Long tmdbid;
    public String writer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public String title;
    public String content;
    public int views;
    public Boolean isPrivate;
    public Boolean isTv;
    public List<NodeDTO> nodes;
    public List<EdgeDTO> edges;

    public BoardDTO(Long id, Long tmdbid, String writer, LocalDateTime createdAt, LocalDateTime updatedAt, String title, String content, int views, Boolean isPrivate, Boolean isTv){
        this.id=id;
        this.tmdbid=tmdbid;
        this.writer=writer;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
        this.title=title;
        this.content=content;
        this.views=views;
        this.isPrivate=isPrivate;
        this.isTv=isTv;
        this.nodes=null;
        this.edges=null;
    }
}
