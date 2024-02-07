package com.movieworld.movieboard.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EdgeDTO {
    private Long id;
    private Long from;
    private Long to;
    private int type;
    private String details;

    public EdgeDTO(Long id, Long To, Long From, String Details){
        this.id=id;
        this.to=To;
        this.from=From;
        this.details=Details;
    }


}
