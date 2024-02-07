package com.movieworld.movieboard.controller;

import com.movieworld.movieboard.DTO.BoardDTO;
import com.movieworld.movieboard.DTO.MemberDTO;
import com.movieworld.movieboard.DTO.Pagination;
import com.movieworld.movieboard.Service.BoardService;
import com.movieworld.movieboard.domain.Board;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/newBoard")
    @ResponseBody
    Long newBoard(@RequestBody BoardDTO boardDTO){
        Long boardID=boardService.AddBoard(boardDTO);
        return boardID;
    }

    @GetMapping("/Boardlist/{no}")
    @ResponseBody
    Map<String, Object> boardList(@PathVariable("no") Long pn) {
        List<BoardDTO> boardList = boardService.ReturnBoard(pn);

        Map<String, Object> response = new HashMap<>();
        response.put("data", boardList);

        Pagination pagination=boardService.pngn(pn);

        response.put("pagination", pagination);

        return response;
    }




}
