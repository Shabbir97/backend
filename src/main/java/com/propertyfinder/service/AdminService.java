package com.propertyfinder.service;

import com.propertyfinder.dto.AdminStatsDto;
import com.propertyfinder.entity.Property;
import com.propertyfinder.entity.TenantRequest;
import com.propertyfinder.entity.User;
import com.propertyfinder.enums.Role;
import com.propertyfinder.repository.PropertyRepository;
import com.propertyfinder.repository.TenantRequestRepository;
import com.propertyfinder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final TenantRequestRepository tenantRequestRepository;

    // VIEW ALL USERS
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // VIEW ALL PROPERTIES
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    // DELETE ANY PROPERTY
    public void deleteProperty(Long propertyId) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() ->
                        new RuntimeException("Property not found"));

        propertyRepository.delete(property);
    }

    // BAN USER
    public User banUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setActive(false);

        return userRepository.save(user);
    }

    // UNBAN USER
    public User unbanUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setActive(true);

        return userRepository.save(user);
    }

    // APPROVE OWNER
    public User approveOwner(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (user.getRole() != Role.OWNER) {
            throw new RuntimeException("User is not an owner");
        }

        user.setApproved(true);

        return userRepository.save(user);
    }

    // ADMIN DASHBOARD STATISTICS
    public AdminStatsDto getStats() {

        return AdminStatsDto.builder()
                .totalUsers(userRepository.count())
                .totalOwners(userRepository.countByRole(Role.OWNER))
                .totalTenants(userRepository.countByRole(Role.TENANT))
                .totalProperties(propertyRepository.count())
                .totalRequests(tenantRequestRepository.count())
                .build();
    }

    // VIEW ALL TENANT REQUESTS
    public List<TenantRequest> getAllRequests() {
        return tenantRequestRepository.findAll();
    }
}