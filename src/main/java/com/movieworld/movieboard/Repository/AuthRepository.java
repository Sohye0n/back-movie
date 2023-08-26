package com.movieworld.movieboard.Repository;

import com.movieworld.movieboard.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Authority,String> {
}
