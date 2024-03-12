package com.movieworld.movieboard.Repository;

import com.movieworld.movieboard.domain.Comment;
import com.movieworld.movieboard.domain.ReComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReCommentRepository extends JpaRepository<ReComment,Long> {

    @Query("SELECT c FROM ReComment c WHERE c.writer.id = :memberId")
    List<ReComment> findAllByMemberId(Long memberId);

    @Query("SELECT c FROM ReComment c WHERE c.board.id = :boardId")
    List<ReComment> findAllinBoard(Long boardId);
}
