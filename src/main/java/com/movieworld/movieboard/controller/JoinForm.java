package com.movieworld.movieboard.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class JoinForm {
    @NotNull
    private String Nickname;

    @NotNull
    private String pw;
}
