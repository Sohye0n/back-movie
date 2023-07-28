package com.movieworld.movieboard.controller;

import com.movieworld.movieboard.DTO.BoardDTO;
import com.movieworld.movieboard.DTO.MemberDTO;
import com.movieworld.movieboard.Service.BoardService;
import com.movieworld.movieboard.domain.Board;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.apache.coyote.http11.Constants.a;

@Controller
public class HomeController {
    private final BoardService boardService;
    public HomeController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/")
    String Home(){
        return "mainpage";
    }
    @GetMapping("/newBoard")
    String newBoard(){
        return "newBoard";
    }
    @RequestMapping(value="/newBoard",method = RequestMethod.POST)
    String newBoard(@ModelAttribute BoardDTO boardDTO, RedirectAttributes redirectAttributes){
        Long boardID=boardService.AddBoard(boardDTO);
        redirectAttributes.addAttribute("BoardID",boardID);
        System.out.println(boardID);
        return "redirect:/network";
    }

    @GetMapping("/Boards")
    String Boards(RedirectAttributes attributes){
        String pn="1";
        attributes.addAttribute("pn",1);
        return "redirect:/Boardlist";
    }

    @GetMapping("/Boardlist")
    String Boardlist(HttpServletRequest request, Model model){
        Long pn= Long.valueOf(request.getParameter("pn"));
        List boardlist=boardService.ReturnBoard(pn);
        model.addAttribute("boardlist",boardlist);
        model.addAttribute("pn",pn+1);
        return "BoardList";
    }

    @GetMapping("/Boardlist/{no}")
    String Boardlist(@PathVariable("no")Long pn, Model model){
        List boardlist=boardService.ReturnBoard(pn);
        model.addAttribute("boardlist",boardlist);
        model.addAttribute("pn",pn+1);
        return "BoardList";
    }

}
