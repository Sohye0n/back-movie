package com.movieworld.movieboard.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDTO {
    private String title;
    private String content;
    private String isPrivate;
    private Long id;
}
