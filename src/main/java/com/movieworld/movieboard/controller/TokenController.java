package com.movieworld.movieboard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TokenController {
    @PostMapping("/refresh")
    public ResponseEntity refresh(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
