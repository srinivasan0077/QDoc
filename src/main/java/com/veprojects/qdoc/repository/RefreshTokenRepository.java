package com.veprojects.qdoc.repository;

import com.veprojects.qdoc.entities.RefreshToken;
import com.veprojects.qdoc.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    List<RefreshToken> findByUser(User user);

    List<RefreshToken> findByUserId(Long userId);

    long countByUser(User user);
}
