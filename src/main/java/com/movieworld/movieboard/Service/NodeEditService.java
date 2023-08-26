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
        System.out.println("EditNode is running...");
        for(int i=0; i<nodelist.size(); i++){
            NodeDTO curNode=nodelist.get(i);
            System.out.println(curNode.getId());
            int type=curNode.getType();

            //add
            if(type==0){
                Board board=boardRepository.findById(BoardID).orElseThrow(()->new Exception());
                Node newNode=new Node(curNode.getId(), board, curNode.isHub(),curNode.getPhotoUrl(),curNode.getAuthorID(),curNode.getName(),curNode.getDetails());
                System.out.println(newNode.getId());
                nodeRepository.save(newNode);
            }

            //delete
            else if(type==1){
                Node delNode=nodeRepository.findById(curNode.getId()).orElseThrow(()->new IllegalStateException("존재하지 않는 노드"));
                nodeRepository.delete(delNode);
            }

            //update
            else if(nodelist.get(i).getType()==2){
                Node updateNode=nodeRepository.findById(curNode.getId()).orElseThrow(()->new IllegalStateException("존재하지 않는 노드"));
                updateNode.update(curNode.isHub(), curNode.getPhotoUrl(), curNode.getAuthorID(), curNode.getName(), curNode.getDetails());
                nodeRepository.save(updateNode); //똑같이 save를 호출해도 내부적으로 업데이트로 처리해줌.
            }
        }
    }

    public ArrayList<Node> GetNode(Long BoardID){
        ArrayList<Node>nodelist= (ArrayList<Node>) nodeRepository.findByBoard(BoardID);
        System.out.println("node edit service is running...\n");
        for (Node node:nodelist) {
            System.out.println(node.getId());
        }
        return nodelist;
    }
}
