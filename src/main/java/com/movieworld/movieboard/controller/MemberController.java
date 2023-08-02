package com.movieworld.movieboard.controller;

import com.movieworld.movieboard.DTO.MemberDTO;
import com.movieworld.movieboard.DTO.TokenDTO;
import com.movieworld.movieboard.Jwt.JwtFilter;
import com.movieworld.movieboard.Jwt.TokenProvider;
import com.movieworld.movieboard.Service.MemberLoginService;
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping("/login")
    public String Login(HttpServletRequest httpServletRequest, Model model){
        System.out.println("login");
        String errno=httpServletRequest.getParameter("error");
        if(errno==null) return "newMember";
        System.out.println(errno);
        model.addAttribute("errno",errno);
        return "loginexceed";
    }

    @PostMapping("/LoginForm")
    public ResponseEntity<TokenDTO> processingLoginForm(@ModelAttribute MemberDTO memberDTO) throws Exception {
        System.out.println("loginform");
        System.out.println(memberDTO.getNickname());
        System.out.println(memberDTO.getPw());
        boolean isOurMember= memberLoginService.Login(memberDTO);

        //회원이 맞다면 jwt 토큰을 발행함

            //일단 usernamepasswordtoken을 발행하는데, 이는 아직 권한 부여가 되지 않은 토큰.
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(memberDTO.getNickname(),memberDTO.getPw());
            System.out.println("UsernamePasswordToken created");

            //이 토큰에게 권한을 부여해줌
            Authentication authentication=authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);
            System.out.println("authentication succ");
            //context holder 에 이 유저에 대한 정보를 저장함.
            SecurityContextHolder.getContext().setAuthentication(authentication);

            //이제 권한을 부여받은 이 토큰을 가지고 jwt를 생성해줌.
            String jwt= tokenProvider.createToken(authentication);
            System.out.println(jwt);

            HttpHeaders httpHeaders=new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER,"Bearer"+jwt);

            return new ResponseEntity<>(new TokenDTO(jwt),  httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/join")
    public String Join(HttpServletRequest httpServletRequest, Model model){
        return "Join";
    }

    @PostMapping("/JoinForm")
    public String JoinForm(MemberDTO memberDTO){
        System.out.println(memberDTO.getNickname());
        System.out.println(memberDTO.getPw());
        memberLoginService.Join(memberDTO);
        return "redirect:/login";
    }

}
