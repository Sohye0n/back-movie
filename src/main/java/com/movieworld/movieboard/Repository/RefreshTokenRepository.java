package com.movieworld.movieboard.Repository;

import com.movieworld.movieboard.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}
