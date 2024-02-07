package com.movieworld.movieboard.Service;

import com.movieworld.movieboard.DTO.EdgeDTO;
import com.movieworld.movieboard.DTO.NodeDTO;
import com.movieworld.movieboard.Repository.BoardRepository;
import com.movieworld.movieboard.Repository.EdgeRepository;
import com.movieworld.movieboard.Repository.NodeRepository;
import com.movieworld.movieboard.domain.Board;
import com.movieworld.movieboard.domain.Edge;
import com.movieworld.movieboard.domain.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EdgeEditService {
    @Autowired
    private final EdgeRepository edgeRepository;
    @Autowired
    private final BoardRepository boardRepository;
    @Autowired
    private final NodeRepository nodeRepository;

    public EdgeEditService(EdgeRepository edgeRepository, BoardRepository boardRepository, NodeRepository nodeRepository) {
        this.edgeRepository=edgeRepository;
        this.boardRepository = boardRepository;
        this.nodeRepository = nodeRepository;
    }

    public void EditEdge(List<EdgeDTO> edgelist, Long BoardID) throws Exception {
        for (int i = 0; i < edgelist.size(); i++) {
            EdgeDTO curEdge = edgelist.get(i);
            int type = curEdge.getType();
            //check if board is valid
            Board board = boardRepository.findById(BoardID).orElseThrow(() -> new IllegalStateException("존재하지 않는 게시글"));
            System.out.println(curEdge.getFrom());

            //add
            if (type == 0) {
                Node From = nodeRepository.findByIdAndBoard(curEdge.getFrom(),BoardID);
                Node To = nodeRepository.findByIdAndBoard(curEdge.getTo(),BoardID);
                Edge newEdge = new Edge(curEdge.getId(), From.getNodeId(), To.getNodeId(), curEdge.getDetails(), board);
                edgeRepository.save(newEdge);
            }

            //edit details
            else if (type == 1) {
                Edge updateEdge = edgeRepository.findById(curEdge.getId()).orElseThrow(() -> new IllegalStateException("존재하지 않는 엣지"));
                updateEdge.updateDetail(curEdge.getDetails());
                edgeRepository.save(updateEdge);
            }
        }
    }

    public List<EdgeDTO> GetEdge(Long BoardID){
        List<Edge>edgelist= edgeRepository.findByBoard(BoardID);
        List<EdgeDTO>edgeDTOList=edgelist.stream().
                map(edge->new EdgeDTO(
                        edge.getEdgeId(),
                        edge.getToNode(),
                        edge.getFromNode(),
                        edge.getDetails()
                ))
                .collect(Collectors.toList());
        return edgeDTOList;
    }
}
