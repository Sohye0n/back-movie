package com.movieworld.movieboard.Repository;

import com.movieworld.movieboard.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {
    @Modifying
    @Query("UPDATE Board b SET b.views = b.views + 1 WHERE b.id = :boardId")
    void updateViews(Long boardId);

    @Query("SELECT b FROM Board b WHERE b.writer.id = :memberId")
    List<Board> findAllByMemberId(Long memberId);
}
