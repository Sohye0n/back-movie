package com.movieworld.movieboard.controller;

import com.movieworld.movieboard.DTO.CommentDTO;
import com.movieworld.movieboard.Service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment/add/{no}")
    public ResponseEntity addComment(@RequestBody CommentDTO commentDTO, @PathVariable("no")Long boardId){
        commentService.addComment(commentDTO,boardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/comment/del/{no}")
    public void delComment(@RequestBody Long commentId, @RequestBody Boolean isRef){
        commentService.delComment(commentId,isRef);
    }

    @PostMapping("/comment/get/{no}")
    public List<CommentDTO> getComment(@RequestBody Long boardId){
        return commentService.getComment(boardId);
    }
}
