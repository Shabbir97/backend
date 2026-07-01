package com.propertyfinder.service;

import com.propertyfinder.entity.Favorite;
import com.propertyfinder.entity.Property;
import com.propertyfinder.entity.User;
import com.propertyfinder.repository.FavoriteRepository;
import com.propertyfinder.repository.PropertyRepository;
import com.propertyfinder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    @Transactional(readOnly = true)
    public List<Property> getFavorites(String email) {
        User user = getUser(email);
        return favoriteRepository.findByUser(user)
                .stream()
                .map(Favorite::getProperty)
                .collect(Collectors.toList());
    }

    public void addFavorite(Long propertyId, String email) {
        User user = getUser(email);
        Property property = getProperty(propertyId);

        if (favoriteRepository.existsByUserAndProperty(user, property)) {
            return; // already favorited, skip silently
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .property(property)
                .build();

        favoriteRepository.save(favorite);
    }

    public void removeFavorite(Long propertyId, String email) {
        User user = getUser(email);
        Property property = getProperty(propertyId);

        Favorite favorite = favoriteRepository
                .findByUserAndProperty(user, property)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Favorite not found"));

        favoriteRepository.delete(favorite);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
    }

    private Property getProperty(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Property not found"));
    }
}