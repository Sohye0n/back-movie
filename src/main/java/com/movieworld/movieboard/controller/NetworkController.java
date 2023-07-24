package com.movieworld.movieboard.controller;

import com.movieworld.movieboard.DTO.NodeDTO;
import com.movieworld.movieboard.Repository.BoardRepository;
import com.movieworld.movieboard.Service.BoardService;
import com.movieworld.movieboard.Service.NodeEditService;
import com.movieworld.movieboard.domain.Board;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.movieworld.movieboard.domain.Node;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

@Controller
public class NetworkController {
    private final NodeEditService nodeEditService;
    private final BoardService boardService;
    private final BoardRepository boardRepository;

    public NetworkController(NodeEditService nodeEditService, BoardService boardService, BoardRepository boardRepository) {
        this.nodeEditService = nodeEditService;
        this.boardService = boardService;
        this.boardRepository = boardRepository;
    }
    class BoardFindingException extends RuntimeException{

    }

    @GetMapping("/network")
    String network(Model model, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) throws Exception {
        Long BoardID= Long.valueOf(httpServletRequest.getParameter("BoardID"));
        Board board=boardRepository.findById(Long.valueOf(httpServletRequest.getParameter("BoardID"))).orElseThrow(()->new Exception());
        System.out.println(BoardID);
        Node node1=new Node("0", board,false, "minions_bob.jpg","writer","bob","it's minions bob!");
        Node node2=new Node("1", board,false,null,"writer","second","this is second node");
        Node node3=new Node("2", board,false,null,"writer","third","this is third node");
        ArrayList<Node> nodes=new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        model.addAttribute("nodeList",nodes);
        model.addAttribute("BoardID",BoardID);
        redirectAttributes.addAttribute("BoardID",BoardID);
        return "network_sample";
    }
    @PostMapping("/network/board/{no}")
    String saveNetwork(@RequestBody ArrayList<NodeDTO> nodeList, @PathVariable("no")Long no) throws Exception {
        System.out.println("boardID");
        for(NodeDTO element: nodeList){
            System.out.println(element.getType());
            System.out.println(element.getName());
            System.out.println(element.getDetails());
            nodeEditService.EditNode(nodeList,no);
        }
        return "redirect:/";
    }
}
