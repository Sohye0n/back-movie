package com.movieworld.movieboard.Repository;

import com.movieworld.movieboard.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT m FROM Member m WHERE m.nickname = :name")
    Member findByNickname(String name);

    @EntityGraph(attributePaths = "authorities")
    Optional<Member> findOneWithAuthoritiesByNickname(String nickname);
}
