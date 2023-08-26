package com.movieworld.movieboard.Repository;

import com.movieworld.movieboard.domain.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NodeRepository extends JpaRepository<Node,String> {

    @Query("SELECT a FROM Node a WHERE a.Board.id = :id")
    List<Node> findByBoard(Long id);

}
