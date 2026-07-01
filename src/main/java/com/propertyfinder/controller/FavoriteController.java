package com.propertyfinder.controller;

import com.propertyfinder.entity.Property;
import com.propertyfinder.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public List<Property> getFavorites(Authentication authentication) {
        return favoriteService.getFavorites(authentication.getName());
    }

    @PostMapping("/{propertyId}")
    public ResponseEntity<Void> addFavorite(
            @PathVariable Long propertyId,
            Authentication authentication
    ) {
        favoriteService.addFavorite(propertyId, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{propertyId}")
    public ResponseEntity<Void> removeFavorite(
            @PathVariable Long propertyId,
            Authentication authentication
    ) {
        favoriteService.removeFavorite(propertyId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}