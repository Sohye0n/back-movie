package com.movieworld.movieboard.Service;

import com.movieworld.movieboard.DTO.BoardDTO;
import com.movieworld.movieboard.DTO.Pagination;
import com.movieworld.movieboard.Repository.BoardRepository;
import com.movieworld.movieboard.Repository.MemberRepository;
import com.movieworld.movieboard.domain.Board;
import com.movieworld.movieboard.domain.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    public BoardService(BoardRepository boardRepository, MemberRepository memberRepository) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    public Map GetTMDBIdANDIsTV(Long BoardId){
        Board board= boardRepository.findById(BoardId).orElseThrow(IllegalAccessError::new);
        Map<String, Object> result=new HashMap<>();
        result.put("TMDBId",board.getTmdbId());
        result.put("isTV",board.getIsTv());
        return result;
    }

    private Member getWriter(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication=context.getAuthentication();
        String username= authentication.getName();
        return memberRepository.findByNickname(username);
    }


    public Long AddBoard(BoardDTO boardDTO){
        boolean isprivate;
        if(boardDTO.getIsPrivate()==true) isprivate=true;
        else isprivate=false;
        Board board = new Board(boardDTO.getTmdbid(), getWriter(), boardDTO.getContent(), LocalDateTime.now(),LocalDateTime.now(),boardDTO.getTitle(),boardDTO.getContent(),0,boardDTO.getIsPrivate(),boardDTO.getIsTv());
        boardRepository.save(board);
        return board.getBoardId();
    }

    public List<BoardDTO> ReturnBoard(Long pn){
        List<Board>boardList=boardRepository.findAll();
        List<BoardDTO> boardDTOList = boardList.stream()
                .map(board -> new BoardDTO(
                        board.getBoardId(),
                        board.getTmdbId(),
                        board.getWriter(),
                        board.getCreatedAt(),
                        board.getUpdatedAt(),
                        board.getTitle(),
                        board.getContent(),
                        board.getViews(),
                        board.getIsPrivate(),
                        board.getIsTv()
                ))
                .collect(Collectors.toList());

        List<BoardDTO>returnList=new ArrayList<BoardDTO>();
        Long size= Long.valueOf(boardDTOList.size());
        if(size>5*pn) size=5*pn;
        for(Long i=(pn-1)*5; i<size; i++) returnList.add(boardDTOList.get(i.intValue()));
        return returnList;
    }

    public Pagination pngn(Long pn){
        List<Board>boardList=boardRepository.findAll();
        Pagination pngn=new Pagination();
        int blockcnt=boardList.size()/5+1;
        int curblock= Math.toIntExact(pn);

        pngn.setEndPage(5);
        if(curblock==1) pngn.setPrevBlock(1);
        else pngn.setPrevBlock(curblock-1);

        pngn.setNextBlock(curblock+1);
        pngn.setStartPage(1);
        pngn.setTotalPageCnt(blockcnt);
        return pngn;
    }

    public Optional<Board> FindById(Long id){
        Optional<Board> board=boardRepository.findById(id);
        return board;
    }

}
