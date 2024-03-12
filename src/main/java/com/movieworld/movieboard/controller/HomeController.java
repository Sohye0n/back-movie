package com.movieworld.movieboard.controller;

import com.movieworld.movieboard.DTO.BoardDTO;
import com.movieworld.movieboard.DTO.CommentDTO;
import com.movieworld.movieboard.DTO.Pagination;
import com.movieworld.movieboard.Service.BoardService;
import com.movieworld.movieboard.Service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    private final BoardService boardService;
    private final CommentService commentService;

    public HomeController(BoardService boardService, CommentService commentService) {
        this.boardService = boardService;
        this.commentService = commentService;
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
        List<BoardDTO> boardList = boardService.returnBoardList(pn);

        Map<String, Object> response = new HashMap<>();
        response.put("data", boardList);

        Pagination pagination=boardService.pngn(pn);

        response.put("pagination", pagination);

        return response;
    }

    @GetMapping("/mypage")
    public @ResponseBody Map<String, Object> mypage(){
        Map<String, Object> response = new HashMap<>();
        List<BoardDTO> boardList=boardService.returnMyBoards();
        List<CommentDTO> commentList=commentService.returnMyComments();
        response.put("boards",boardList);
        response.put("comments",commentList);
        return response;
    }




}
