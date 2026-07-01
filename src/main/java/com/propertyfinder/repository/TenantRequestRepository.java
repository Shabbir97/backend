package com.propertyfinder.repository;

import com.propertyfinder.entity.TenantRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TenantRequestRepository
        extends JpaRepository<TenantRequest, Long> {

    List<TenantRequest> findByTenantEmail(String email);

    List<TenantRequest> findByPropertyOwnerEmail(String email);
}