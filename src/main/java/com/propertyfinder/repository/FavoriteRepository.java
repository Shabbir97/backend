package com.propertyfinder.repository;

import com.propertyfinder.entity.Favorite;
import com.propertyfinder.entity.Property;
import com.propertyfinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUser(User user);

    Optional<Favorite> findByUserAndProperty(User user, Property property);

    boolean existsByUserAndProperty(User user, Property property);

    void deleteByUserAndProperty(User user, Property property);
}