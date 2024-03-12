package com.movieworld.movieboard.Service;

import com.movieworld.movieboard.DTO.BoardDTO;
import com.movieworld.movieboard.DTO.Pagination;
import com.movieworld.movieboard.Repository.*;
import com.movieworld.movieboard.domain.*;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.ListOperations;
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
    private final NodeRepository nodeRepository;
    private final EdgeRepository edgeRepository;
    private final CommentRepository commentRepository;
    private final ReCommentRepository reCommentRepository;
    @Resource(name="redisTemplate")
    private ListOperations<String, String> listOps;


    public BoardService(BoardRepository boardRepository, MemberRepository memberRepository, NodeRepository nodeRepository, EdgeRepository edgeRepository, CommentRepository commentRepository, ReCommentRepository reCommentRepository) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
        this.nodeRepository = nodeRepository;
        this.edgeRepository = edgeRepository;
        this.commentRepository = commentRepository;
        this.reCommentRepository = reCommentRepository;
    }

    public Map getTmdbIdandIsTv(Long boardId){
        Board board= boardRepository.findById(boardId).orElseThrow(IllegalAccessError::new);

        Map<String, Object> result=new HashMap<>();
        result.put("TMDBId",board.getTmdbId());
        result.put("isTV",board.getIsTv());
        result.put("title",board.getTitle());
        result.put("writer",board.getWriter().getNickname());

        updateViews(boardId);

        return result;
    }

    private Member getCurrentUser(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication=context.getAuthentication();
        String username= authentication.getName();
        System.out.println(username);
        return memberRepository.findByNickname(username);
    }


    public Long AddBoard(BoardDTO boardDTO){
        boolean isprivate;
        if(boardDTO.getIsPrivate()==true) isprivate=true;
        else isprivate=false;
        Board board = new Board(boardDTO.getTmdbid(), getCurrentUser(), boardDTO.getTitle(), LocalDateTime.now(),LocalDateTime.now(),boardDTO.getTitle(),boardDTO.getContent(),0,boardDTO.getIsPrivate(), boardDTO.getIsTv());
        boardRepository.save(board);
        return board.getBoardId();
    }

    public List<BoardDTO> returnBoardList(Long pn){
        List<Board>boardList=boardRepository.findAll();
        List<BoardDTO> boardDTOList = boardList.stream()
                .map(board -> new BoardDTO(
                        board.getBoardId(),
                        board.getTmdbId(),
                        board.getWriter().getNickname(),
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
        if(size>10*pn) size=10*pn;
        for(Long i=(pn-1)*10; i<size; i++) returnList.add(boardDTOList.get(i.intValue()));
        return returnList;
    }

    public Pagination pngn(Long pn){
        List<Board>boardList=boardRepository.findAll();
        Pagination pngn=new Pagination();
        int blockcnt=boardList.size()/10+1;
        int curblock= Math.toIntExact(pn);

        pngn.setEndPage(5);
        if(curblock==1) pngn.setPrevBlock(1);
        else pngn.setPrevBlock(curblock-1);

        pngn.setNextBlock(curblock+1);
        pngn.setStartPage(1);
        pngn.setTotalPageCnt(blockcnt);
        return pngn;
    }

    private void updateViews(Long boardId){
        String id=Long.toString(boardId);
        //1. redis에 user의 nickname에 해당하는 value들을 가져온다.
        String nickname=getCurrentUser().getNickname();
        List<String> values=listOps.range(nickname,0,-1);
        //2. list 안에 boardId가 있는지 확인한다.
        for(int i=0; i< values.size(); i++){
            System.out.println(values.get(i));
            if(values.get(i).equals(id)) return;
        }
        //3. boardId가 없으면 db에 저장한 후 view를 업데이트한다.
        listOps.leftPush(nickname,id);
        boardRepository.updateViews(boardId);
    }

    public List<BoardDTO> returnMyBoards() {
        //1. 현재 로그인한 사용자를 security context에서 찾는다.
        Member curUser=getCurrentUser();
        //2. 사용자와 연결된 board를 찾는다.
        //3. board를 forEach를 사용하여 boardDTO로 바꾼다.
        List<BoardDTO>boardDTOList=new ArrayList<>();
        boardRepository.findAllByMemberId(curUser.getId()).forEach((board)->{
            BoardDTO boardDTO=new BoardDTO(
                    board.getBoardId(),
                    board.getTmdbId(),
                    curUser.getNickname(),
                    board.getCreatedAt(),
                    board.getUpdatedAt(),
                    board.getTitle(),
                    board.getContent(),
                    board.getViews(),
                    board.getIsPrivate(),
                    board.getIsTv()
            );
            boardDTOList.add(boardDTO);
        });
        //4. 리턴한다.
        return boardDTOList;
    }

    public void delBoard(Long no) {
        //1. delete edge
        List<Edge> edges=new ArrayList<>();
        edges=edgeRepository.findByBoard(no);
        edges.forEach(edge->{
            edgeRepository.delete(edge);
        });

        //2. delete node
        List<Node> nodes=new ArrayList<>();
        nodes=nodeRepository.findByBoard(no);
        nodes.forEach(node->{
            nodeRepository.delete(node);
        });

        //3. delete reComment & comment
        List <ReComment> reComments=reCommentRepository.findAllinBoard(no);
        reComments.forEach(reComment -> {
            reCommentRepository.delete(reComment);
        });

        List<Comment> comments=commentRepository.findAllinBoard(no);
        comments.forEach(comment -> {
            commentRepository.delete(comment);
        });

        //4. delete Board
        Board board=boardRepository.findById(no).orElseThrow();
        boardRepository.delete(board);
    }
}
