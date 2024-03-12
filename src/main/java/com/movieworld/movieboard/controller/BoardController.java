package com.movieworld.movieboard.controller;

import com.movieworld.movieboard.DTO.EdgeDTO;
import com.movieworld.movieboard.DTO.NodeDTO;
import com.movieworld.movieboard.Service.BoardService;
import com.movieworld.movieboard.Service.EdgeEditService;
import com.movieworld.movieboard.Service.NodeEditService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BoardController {
    private final BoardService boardService;
    private final NodeEditService nodeEditService;
    private final EdgeEditService edgeEditService;

    public BoardController(BoardService boardService, NodeEditService nodeEditService, EdgeEditService edgeEditService) {
        this.boardService = boardService;
        this.nodeEditService = nodeEditService;
        this.edgeEditService = edgeEditService;
    }

    @GetMapping("/board/view/{no}")
    @ResponseBody
    Map TitleClick_view(@PathVariable("no")Long no, HttpServletResponse httpServletResponse){
        Map boardInfo=boardService.getTmdbIdandIsTv(no);
        List<NodeDTO>nodes=nodeEditService.GetNode(no); //해당 게시글에 있었던 노드 전부 가져오기
        List<EdgeDTO>edges=edgeEditService.GetEdge(no); //해당 게시글에 있었던 엣지 전부 가져오기

        Map<String, Object> result = new HashMap<>();
        result.put("tmdbId",boardInfo.get("TMDBId"));
        result.put("isTv",boardInfo.get("isTV"));
        result.put("title",boardInfo.get("title"));
        result.put("writer",boardInfo.get("writer"));
        result.put("nodes", nodes);
        result.put("edges", edges);

        return result;
    }

//    @GetMapping("board/edit/{no}")
//    String TitleClick_edit(@PathVariable("no")Long no, Model model){
//        List<NodeDTO>nodes=nodeEditService.GetNode(no); //해당 게시글에 있었던 노드 전부 가져오기
//        model.addAttribute("nodeList",nodes);
//        model.addAttribute("BoardID",no);
//        return "network_sample";
//    }

    @GetMapping("board/del/{no}")
    void delBoard(@PathVariable("no")Long no, HttpServletResponse response){
        boardService.delBoard(no);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}