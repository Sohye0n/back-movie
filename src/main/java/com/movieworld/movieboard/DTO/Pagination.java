package com.movieworld.movieboard.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pagination {
    int endPage;
    int nextBlock;
    int prevBlock;
    int startPage;
    int totalPageCnt;
}
