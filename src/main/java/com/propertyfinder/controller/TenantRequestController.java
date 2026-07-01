package com.propertyfinder.controller;

import com.propertyfinder.dto.TenantRequestDto;
import com.propertyfinder.entity.TenantRequest;
import com.propertyfinder.service.TenantRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class TenantRequestController {

    @Autowired
    private TenantRequestService tenantRequestService;

    // SEND REQUEST
    @PostMapping
    public TenantRequest sendRequest(
            @RequestBody TenantRequestDto dto,
            Authentication authentication
    ) {

        return tenantRequestService.sendRequest(
                dto,
                authentication.getName()
        );
    }

    // TENANT VIEWS OWN REQUESTS
    @GetMapping("/my")
    public List<TenantRequest> getMyRequests(
            Authentication authentication
    ) {

        return tenantRequestService.getMyRequests(
                authentication.getName()
        );
    }

    // OWNER VIEWS REQUESTS
    @GetMapping("/owner")
    public List<TenantRequest> getOwnerRequests(
            Authentication authentication
    ) {

        return tenantRequestService.getOwnerRequests(
                authentication.getName()
        );
    }

    // APPROVE REQUEST
    @PutMapping("/{id}/approve")
    public TenantRequest approveRequest(
            @PathVariable Long id,
            Authentication authentication
    ) {

        return tenantRequestService.approveRequest(
                id,
                authentication.getName()
        );
    }

    // REJECT REQUEST
    @PutMapping("/{id}/reject")
    public TenantRequest rejectRequest(
            @PathVariable Long id,
            Authentication authentication
    ) {

        return tenantRequestService.rejectRequest(
                id,
                authentication.getName()
        );
    }
}