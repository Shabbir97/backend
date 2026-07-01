package com.propertyfinder.repository;

import com.propertyfinder.entity.RefreshToken;
import com.propertyfinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    /**
     * Find a refresh token by its token string.
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Find the refresh token belonging to a specific user.
     */
    Optional<RefreshToken> findByUser(User user);

    /**
     * Delete a user's refresh token.
     * Used during logout.
     */
    void deleteByUser(User user);

    /**
     * Remove expired refresh tokens.
     * Useful for scheduled cleanup jobs.
     */
    void deleteByExpiryDateBefore(LocalDateTime date);
}