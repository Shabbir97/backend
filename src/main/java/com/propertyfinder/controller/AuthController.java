package com.propertyfinder.controller;

import com.propertyfinder.dto.AuthResponse;
import com.propertyfinder.dto.LoginRequest;
import com.propertyfinder.dto.RefreshTokenRequest;
import com.propertyfinder.dto.RegisterRequest;
import com.propertyfinder.dto.UpdateProfileRequest;
import com.propertyfinder.dto.UserProfileResponse;
import com.propertyfinder.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // REGISTER
    @PostMapping("/register")
    public AuthResponse register(
            @RequestBody RegisterRequest request
    ) {
        return authService.register(request);
    }

    // LOGIN
    @PostMapping("/login")
    public AuthResponse login(
            @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

    // REFRESH TOKEN
    @PostMapping("/refresh")
    public AuthResponse refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {
        return authService.refreshToken(request);
    }

    // CURRENT USER PROFILE
    @GetMapping("/me")
    public UserProfileResponse me(
            Authentication authentication
    ) {
        return authService.getCurrentUser(
                authentication.getName()
        );
    }

    // UPDATE PROFILE
    @PutMapping("/me")
    public UserProfileResponse updateProfile(
            @RequestBody UpdateProfileRequest request,
            Authentication authentication
    ) {
        return authService.updateProfile(
                authentication.getName(),
                request
        );
    }
}