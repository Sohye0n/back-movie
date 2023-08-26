package com.movieworld.movieboard.controller;

import com.movieworld.movieboard.DTO.NodeDTO;
import com.movieworld.movieboard.Repository.BoardRepository;
import com.movieworld.movieboard.Service.BoardService;
import com.movieworld.movieboard.Service.NodeEditService;
import com.movieworld.movieboard.domain.Board;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.movieworld.movieboard.domain.Node;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@RestController
public class NetworkController {
    private final NodeEditService nodeEditService;
    private final BoardService boardService;
    private final BoardRepository boardRepository;

    public NetworkController(NodeEditService nodeEditService, BoardService boardService, BoardRepository boardRepository) throws Exception {
        this.nodeEditService = nodeEditService;
        this.boardService = boardService;
        this.boardRepository = boardRepository;
    }
    class BoardFindingException extends RuntimeException{

    }

    @GetMapping("/network/{no}")
    @ResponseBody
    ArrayList<Node> network(@PathVariable("no")Long no) throws Exception {
        Long BoardID= no;
        Board board=boardRepository.findById(no).orElseThrow(()->new Exception());

        Node node1=new Node("0", board,false, "minions_bob.jpg","writer","bob","it's minions bob!");
        Node node2=new Node("1", board,false,null,"writer","second","this is second node");
        Node node3=new Node("2", board,false,null,"writer","third","this is third node");
        ArrayList<Node> NodeList=new ArrayList<>();
        NodeList.add(node1);
        NodeList.add(node2);
        NodeList.add(node3);

        return NodeList;
    }

    @PostMapping("/network/save/{no}")
    void saveNetwork(@RequestBody List<NodeDTO> nodeList, @PathVariable("no")Long no) throws Exception {
        for(NodeDTO element: nodeList){
            System.out.println(element.getType());
            System.out.println(element.getName());
            System.out.println(element.getDetails());
            nodeEditService.EditNode(nodeList,no);
        }
    }
}
