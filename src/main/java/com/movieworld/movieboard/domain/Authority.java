package com.movieworld.movieboard.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Authority {
    @Id
    @Column(name="Authority_name")
    private String authorityName;
}
