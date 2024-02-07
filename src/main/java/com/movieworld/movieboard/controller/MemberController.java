package com.movieworld.movieboard.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieworld.movieboard.DTO.MemberDTO;
import com.movieworld.movieboard.DTO.TokenDTO;
//import com.movieworld.movieboard.Jwt.JwtFilter;
import com.movieworld.movieboard.Jwt.TokenProvider;
import com.movieworld.movieboard.Service.MemberLoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import static com.movieworld.movieboard.Jwt.JwtFilter.AUTHORIZATION_HEADER;

@Controller
public class MemberController {

    private final MemberLoginService memberLoginService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public MemberController(MemberLoginService memberLoginService, TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.memberLoginService = memberLoginService;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/login")
    public ResponseEntity Login(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Model model) throws IOException {
        String errno=httpServletRequest.getParameter("error");
        if(errno==null) return new ResponseEntity<>(HttpStatus.OK);
        System.out.println(errno);
        model.addAttribute("errno",errno);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody MemberDTO memberDTO) throws Exception {
        if(memberLoginService.check(memberDTO)){
            memberLoginService.join(memberDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.CONFLICT);
    }


}
