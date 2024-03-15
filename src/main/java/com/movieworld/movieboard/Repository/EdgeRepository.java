package com.movieworld.movieboard.Repository;

import com.movieworld.movieboard.domain.Edge;
import com.movieworld.movieboard.domain.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EdgeRepository extends JpaRepository<Edge,Long> {
    @Query("SELECT a FROM Edge a WHERE a.Board.id = :id")
    List<Edge> findByBoard(Long id);
}
