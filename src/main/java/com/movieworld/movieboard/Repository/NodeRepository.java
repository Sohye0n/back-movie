package com.movieworld.movieboard.Repository;

import com.movieworld.movieboard.domain.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NodeRepository extends JpaRepository<Node,String> {
    /*@Query(value="SELECT a FROM Node a WHERE a.board.id=ID")
    List<Node> findByboard(Long ID);*/

    @Query("SELECT a FROM Node a WHERE a.board.id = :id")
    List<Node> findByboard(@Param("id") Long id);

}
