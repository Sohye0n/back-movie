package com.movieworld.movieboard.Repository;

import com.movieworld.movieboard.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {

}
