package com.movieworld.movieboard.controller;

import com.movieworld.movieboard.DTO.MemberDTO;
import com.movieworld.movieboard.Service.MemberLoginService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class MemberController {

    private final MemberLoginService memberLoginService;

    public MemberController(MemberLoginService memberLoginService) {
        this.memberLoginService = memberLoginService;
    }

    @GetMapping("/login")
    String Login(HttpServletRequest httpServletRequest, Model model){
        String errno=httpServletRequest.getParameter("error");
        if(errno==null) return "newMember";
        System.out.println(errno);
        model.addAttribute("errno",errno);
        return "loginexceed";
    }

}
