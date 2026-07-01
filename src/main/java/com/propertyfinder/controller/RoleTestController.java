package com.propertyfinder.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleTestController {

    @GetMapping("/api/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin access granted";
    }

    @GetMapping("/api/owner")
    @PreAuthorize("hasRole('OWNER')")
    public String ownerAccess() {
        return "Owner access granted";
    }

    @GetMapping("/api/tenant")
    @PreAuthorize("hasRole('TENANT')")
    public String tenantAccess() {
        return "Tenant access granted";
    }
}