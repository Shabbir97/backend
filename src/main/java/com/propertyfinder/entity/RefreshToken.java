package com.propertyfinder.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Random UUID refresh token
     */
    @Column(nullable = false, unique = true, length = 255)
    private String token;

    /**
     * Expiration date of the refresh token
     */
    @Column(nullable = false)
    private LocalDateTime expiryDate;

    /**
     * Each user owns only one refresh token.
     * If a new one is created, the old one should be replaced.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;
}