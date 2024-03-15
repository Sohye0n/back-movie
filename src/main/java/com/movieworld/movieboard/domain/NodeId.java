package com.movieworld.movieboard.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class NodeId implements Serializable {
    public Long NodeId;
    public Long Board;
}
