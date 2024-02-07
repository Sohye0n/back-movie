package com.movieworld.movieboard.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class EdgeId implements Serializable {
    public Long EdgeId;
    public Long Board;
}
