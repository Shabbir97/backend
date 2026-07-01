package com.propertyfinder.controller;

import com.propertyfinder.dto.AdminStatsDto;
import com.propertyfinder.entity.Property;
import com.propertyfinder.entity.TenantRequest;
import com.propertyfinder.entity.User;
import com.propertyfinder.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // VIEW ALL USERS
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    // VIEW ALL PROPERTIES
    @GetMapping("/properties")
    public List<Property> getAllProperties() {
        return adminService.getAllProperties();
    }

    // DELETE ANY PROPERTY
    @DeleteMapping("/properties/{id}")
    public String deleteProperty(
            @PathVariable Long id
    ) {
        adminService.deleteProperty(id);
        return "Property deleted successfully";
    }

    // BAN USER
    @PutMapping("/users/{id}/ban")
    public User banUser(
            @PathVariable Long id
    ) {
        return adminService.banUser(id);
    }

    // UNBAN USER
    @PutMapping("/users/{id}/unban")
    public User unbanUser(
            @PathVariable Long id
    ) {
        return adminService.unbanUser(id);
    }

    // APPROVE OWNER
    @PutMapping("/owners/{id}/approve")
    public User approveOwner(
            @PathVariable Long id
    ) {
        return adminService.approveOwner(id);
    }

    // ADMIN DASHBOARD STATISTICS
    @GetMapping("/stats")
    public AdminStatsDto getStats() {
        return adminService.getStats();
    }

    // VIEW ALL TENANT REQUESTS
    @GetMapping("/requests")
    public List<TenantRequest> getAllRequests() {
        return adminService.getAllRequests();
    }
}