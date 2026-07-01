package com.propertyfinder.service;

import com.propertyfinder.dto.*;
import com.propertyfinder.entity.RefreshToken;
import com.propertyfinder.entity.User;
import com.propertyfinder.enums.Role;
import com.propertyfinder.repository.RefreshTokenRepository;
import com.propertyfinder.repository.UserRepository;
import com.propertyfinder.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AuthService {

    private static final int REFRESH_TOKEN_EXPIRY_DAYS = 7;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Register a new user.
     */
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists."
            );
        }

        Role role = request.getRole() != null
                ? request.getRole()
                : Role.TENANT;

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .isVerified(false)
                .active(true) // Ensures newly registered users can log in immediately
                .build();

        userRepository.save(user);

        return generateTokens(user);
    }

    /**
     * Generate Access Token + Refresh Token.
     * Replaces any previous refresh token.
     */
    private AuthResponse generateTokens(User user) {

        String accessToken =
                jwtService.generateToken(user.getEmail());

        // Finds old token and forces database deletion immediately
        refreshTokenRepository
                .findByUser(user)
                .ifPresent(token -> {
                    refreshTokenRepository.delete(token);
                    refreshTokenRepository.flush();
                });

        String refreshValue =
                UUID.randomUUID().toString();

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .token(refreshValue)
                        .expiryDate(
                                LocalDateTime.now()
                                        .plusDays(REFRESH_TOKEN_EXPIRY_DAYS)
                        )
                        .user(user)
                        .build();

        refreshTokenRepository.save(refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshValue)
                .build();
    }

    /**
     * Find user by email.
     */
    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User not found."
                        ));
    }

    /**
     * Login an existing user.
     */
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Invalid email or password."
                        ));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password."
            );
        }

        if (!user.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Your account has been disabled."
            );
        }

        return generateTokens(user);
    }

    /**
     * Generate a new access token using a valid refresh token.
     */
    public AuthResponse refreshToken(
            RefreshTokenRequest request
    ) {

        RefreshToken refreshToken =
                refreshTokenRepository
                        .findByToken(request.getRefreshToken())
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.UNAUTHORIZED,
                                        "Invalid refresh token."
                                ));

        if (refreshToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            refreshTokenRepository.delete(refreshToken);

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Refresh token expired."
            );
        }

        return generateTokens(refreshToken.getUser());
    }

    /**
     * Logout current user.
     */
    public void logout(String email) {

        User user = getUserByEmail(email);

        refreshTokenRepository.deleteByUser(user);
    }

    /**
     * Get the currently authenticated user's profile.
     */
    public UserProfileResponse getCurrentUser(String email) {

        User user = getUserByEmail(email);

        return mapToProfile(user);
    }

    /**
     * Update the authenticated user's profile.
     */
    public UserProfileResponse updateProfile(
            String email,
            UpdateProfileRequest request
    ) {

        User user = getUserByEmail(email);

        if (request.getFullName() != null &&
                !request.getFullName().isBlank()) {

            user.setFullName(request.getFullName());
        }

        if (request.getPhone() != null &&
                !request.getPhone().isBlank()) {

            user.setPhone(request.getPhone());
        }

        userRepository.save(user);

        return mapToProfile(user);
    }

    /**
     * Helper method to map User entity to UserProfileResponse DTO.
     */
    private UserProfileResponse mapToProfile(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
    }
}