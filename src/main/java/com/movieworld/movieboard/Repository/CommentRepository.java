package com.movieworld.movieboard.Repository;

import com.movieworld.movieboard.domain.Comment;
import com.movieworld.movieboard.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query("SELECT c FROM Comment c WHERE c.board.id = :boardId")
    List<Comment> findAllinBoard(Long boardId);

}
