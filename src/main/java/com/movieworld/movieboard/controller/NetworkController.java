package com.movieworld.movieboard.controller;

import com.movieworld.movieboard.DTO.EdgeDTO;
import com.movieworld.movieboard.DTO.NodeDTO;
import com.movieworld.movieboard.Repository.BoardRepository;
import com.movieworld.movieboard.Service.BoardService;
import com.movieworld.movieboard.Service.EdgeEditService;
import com.movieworld.movieboard.Service.NodeEditService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NetworkController {
    private final NodeEditService nodeEditService;
    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final EdgeEditService edgeEditService;

    public NetworkController(NodeEditService nodeEditService, BoardService boardService, BoardRepository boardRepository, EdgeEditService edgeEditService) throws Exception {
        this.nodeEditService = nodeEditService;
        this.boardService = boardService;
        this.boardRepository = boardRepository;
        this.edgeEditService = edgeEditService;
    }
    class BoardFindingException extends RuntimeException{};

    @PostMapping("/network/save/nodes/{no}")
    void saveNetworkNode(@RequestBody List<NodeDTO> nodeList, @PathVariable("no")Long no) throws Exception {
        nodeEditService.EditNode(nodeList,no);
    }

    @PostMapping("/network/save/edges/{no}")
    void saveNetworkEdge(@RequestBody List<EdgeDTO> edgeList, @PathVariable("no")Long no) throws Exception {
        edgeEditService.EditEdge(edgeList,no);
    }
}
