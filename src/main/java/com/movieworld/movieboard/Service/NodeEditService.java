package com.movieworld.movieboard.Service;

import com.movieworld.movieboard.Repository.BoardRepository;
import com.movieworld.movieboard.Repository.NodeRepository;
import com.movieworld.movieboard.DTO.NodeDTO;
import com.movieworld.movieboard.domain.Board;
import com.movieworld.movieboard.domain.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NodeEditService {
    @Autowired
    private final NodeRepository nodeRepository;

    @Autowired
    private final BoardRepository boardRepository;

    public NodeEditService(NodeRepository nodeRepository, BoardRepository boardRepository) {
        this.nodeRepository = nodeRepository;
        this.boardRepository = boardRepository;
    }

    public void EditNode(List<NodeDTO> nodelist, Long BoardID) throws Exception {
        for(int i=0; i<nodelist.size(); i++){
            NodeDTO curNode=nodelist.get(i);
            int type=curNode.getType();
            //check if board is valid
            Board board=boardRepository.findById(BoardID).orElseThrow(()->new Exception());

            //add
            if(type==0){
                Node newNode=new Node(curNode.getId(), board, curNode.isDeleted(),curNode.getPhotoUrl(),curNode.getName(),curNode.getDetails());
                nodeRepository.save(newNode);
            }

            //edit
            else if(type==1){
                Node updateNode=nodeRepository.findById(curNode.getId()).orElseThrow(()->new IllegalStateException("존재하지 않는 노드"));
                updateNode.updateName(curNode.getName());
                updateNode.updateDetail(curNode.getDetails());
                nodeRepository.save(updateNode);
            }

            //delete
            else if(type==2){
                Node newNode=new Node(curNode.getId(), board, curNode.isDeleted(),"","","");
                nodeRepository.save(newNode);
            }
        }
    }

    public List<NodeDTO> GetNode(Long BoardID){
        List<Node>nodelist= nodeRepository.findByBoard(BoardID);
        List<NodeDTO>nodeDTOList=nodelist.stream().
                map(node->new NodeDTO(
                        node.getNodeId(),
                        node.getIsDeleted(),
                        node.getPhotoUrl(),
                        node.getName(),
                        node.getDetails()
                ))
                .collect(Collectors.toList());
        return nodeDTOList;
    }
}
