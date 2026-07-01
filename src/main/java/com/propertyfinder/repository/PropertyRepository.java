package com.propertyfinder.repository;

import com.propertyfinder.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface PropertyRepository extends
        JpaRepository<Property, Long>,
        JpaSpecificationExecutor<Property> {

    List<Property> findByOwnerEmail(String email);
}