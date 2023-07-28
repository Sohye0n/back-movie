package com.movieworld.movieboard.Form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class LoginForm {
    @NotNull
    private String id;

    @NotNull
    private String pw;
}
