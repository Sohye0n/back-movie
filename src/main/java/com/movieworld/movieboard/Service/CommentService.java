package com.movieworld.movieboard.Service;

import com.movieworld.movieboard.DTO.CommentDTO;
import com.movieworld.movieboard.Repository.BoardRepository;
import com.movieworld.movieboard.Repository.CommentRepository;
import com.movieworld.movieboard.Repository.MemberRepository;
import com.movieworld.movieboard.Repository.ReCommentRepository;
import com.movieworld.movieboard.domain.Board;
import com.movieworld.movieboard.domain.Comment;
import com.movieworld.movieboard.domain.Member;
import com.movieworld.movieboard.domain.ReComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private final CommentRepository commentRepository;
    @Autowired
    private final ReCommentRepository reCommentRepository;
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final BoardRepository boardRepository;

    public CommentService(CommentRepository commentRepository, ReCommentRepository reCommentRepository, MemberRepository memberRepository, BoardRepository boardRepository) {
        this.commentRepository = commentRepository;
        this.reCommentRepository = reCommentRepository;
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
    }

    private Member getWriter(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication=context.getAuthentication();
        String username= authentication.getName();
        return memberRepository.findByNickname(username);
    }

    private Board getBoard(Long boardId){
        return boardRepository.findById(boardId).orElseThrow();
    }

    public void addComment(CommentDTO commentDTO, Long boardId){
        Board board=getBoard(boardId);
        Member writer=getWriter();

        //댓글
        if(commentDTO.rootId<0){
            Comment comment=new Comment(0L,board,writer,commentDTO.getContent(),LocalDateTime.now(),false);
            commentRepository.save(comment);
        }

        //대댓글
        else{
            Comment rootComment=commentRepository.findById(commentDTO.getRootId()).orElseThrow();
            ReComment reComment=new ReComment(rootComment, (long) rootComment.getReComments().size(),board,writer,commentDTO.getContent(),LocalDateTime.now(),false);
            reCommentRepository.save(reComment);
        }
    }


    public void delComment(Long commentId, Boolean isReComment){

        //댓글
        if(!isReComment){
            Comment comment=commentRepository.findById(commentId).orElseThrow();
            comment.isDeleted=true;
        }

        //대댓글
        else{
            ReComment reComment=reCommentRepository.findById(commentId).orElseThrow();
            reComment.isDeleted=false;
        }
    }


    public List<CommentDTO> getComment(Long boardId){
        Board board=boardRepository.findById(boardId).orElseThrow();
        List<CommentDTO> commentDTOs=new ArrayList<CommentDTO>();
        List<Comment> comments=commentRepository.findAllinBoard(boardId);
        for (Comment comment:comments) {
            //댓글 추가
            CommentDTO commentDTO=new CommentDTO(comment.getCommentId(), (long) -1, (long) -1,comment.getWriter(),comment.getContent(),comment.getCreatedAt(),comment.getIsDeleted());
            commentDTOs.add(commentDTO);

            //대댓글 추가
            comment.getReComments().stream().
                    map(reComment -> new CommentDTO(
                            reComment.getCommentId(),
                            reComment.getRootComment().getCommentId(),
                            reComment.getCnt(),
                            reComment.getWriter(),
                            reComment.getContent(),
                            reComment.getCreatedAt(),
                            reComment.getIsDeleted()
                    ))
            ;
        }
        return commentDTOs;
    }
}
