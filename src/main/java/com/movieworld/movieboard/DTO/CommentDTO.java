package com.movieworld.movieboard.DTO;

import com.movieworld.movieboard.domain.Board;
import com.movieworld.movieboard.domain.Comment;
import com.movieworld.movieboard.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentDTO {
    public Long commentId;
    public Long rootId;
    public Long cnt;
    public Member writer;
    public String content;
    public LocalDateTime createdAt;
    public Boolean isDeleted;
}
