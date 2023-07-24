package com.movieworld.movieboard.Service;

import com.movieworld.movieboard.DTO.BoardDTO;
import com.movieworld.movieboard.Repository.BoardRepository;
import com.movieworld.movieboard.domain.Board;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }


    public Long AddBoard(BoardDTO boardDTO){
        System.out.println("board service is running...\n");
        System.out.println(boardDTO.getTitle());
        boolean isprivate;
        if(boardDTO.getIsPrivate()=="yes") isprivate=true;
        else isprivate=false;
        Board board = new Board(boardDTO.getTitle(),boardDTO.getContent(),isprivate);
        boardRepository.save(board);
        return board.getId();
    }

    public List<Board> ReturnBoard(Long pn){
        List<Board>boardlist=boardRepository.findAll();
        List<Board>returnList=new ArrayList<Board>();
        Long size= Long.valueOf(boardlist.size());
        if(size>5*pn) size=5*pn;
        for(Long i=(pn-1)*5; i<size; i++) returnList.add(boardlist.get(i.intValue()));
        return returnList;
    }

    public Optional<Board> FindById(Long id){
        Optional<Board> board=boardRepository.findById(id);
        return board;
    }

}
