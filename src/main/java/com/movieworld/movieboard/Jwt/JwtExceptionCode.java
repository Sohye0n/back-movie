package com.movieworld.movieboard.Jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtExceptionCode {
    WRONG_TOKEN(1001,"wrong token."),
    EXPIRED_TOKEN(1002,"expired token."),
    UNSUPPORTED_TOKEN(1003,"unsupported token."),
    ILLEGAL_TOKEN(1004,"illegal token.");

    private int code;
    private String message;
}
