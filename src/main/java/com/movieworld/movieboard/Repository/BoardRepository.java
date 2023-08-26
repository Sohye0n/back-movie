package com.movieworld.movieboard.Repository;

import com.movieworld.movieboard.domain.Board;
import com.movieworld.movieboard.domain.Node;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board,Long> {
}
