package com.propertyfinder.service;

import com.propertyfinder.dto.TenantRequestDto;
import com.propertyfinder.entity.Property;
import com.propertyfinder.entity.TenantRequest;
import com.propertyfinder.entity.User;
import com.propertyfinder.enums.RequestStatus;
import com.propertyfinder.repository.PropertyRepository;
import com.propertyfinder.repository.TenantRequestRepository;
import com.propertyfinder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantRequestService {

    private final TenantRequestRepository tenantRequestRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final NotificationService notificationService;

    public TenantRequest sendRequest(TenantRequestDto dto, String tenantEmail) {
        User tenant = getUser(tenantEmail);
        Property property = getProperty(dto.getPropertyId());

        TenantRequest request = TenantRequest.builder()
                .message(dto.getMessage())
                .status(RequestStatus.PENDING)
                .tenant(tenant)
                .property(property)
                .build();

        TenantRequest saved = tenantRequestRepository.save(request);

        // Notify the property owner
        notificationService.create(
                property.getOwner(),
                tenant.getFullName() + " sent a request for \"" + property.getTitle() + "\"",
                "NEW_REQUEST",
                property.getId(),
                saved.getId()
        );

        return saved;
    }

    public List<TenantRequest> getMyRequests(String tenantEmail) {
        return tenantRequestRepository.findByTenantEmail(tenantEmail);
    }

    public List<TenantRequest> getOwnerRequests(String ownerEmail) {
        return tenantRequestRepository.findByPropertyOwnerEmail(ownerEmail);
    }

    public TenantRequest approveRequest(Long requestId, String ownerEmail) {
        TenantRequest request = getRequest(requestId);
        verifyOwner(request, ownerEmail);

        request.setStatus(RequestStatus.APPROVED);
        TenantRequest saved = tenantRequestRepository.save(request);

        // Notify the tenant
        notificationService.create(
                request.getTenant(),
                "Your request for \"" + request.getProperty().getTitle() + "\" was approved!",
                "REQUEST_APPROVED",
                request.getProperty().getId(),
                saved.getId()
        );

        return saved;
    }

    public TenantRequest rejectRequest(Long requestId, String ownerEmail) {
        TenantRequest request = getRequest(requestId);
        verifyOwner(request, ownerEmail);

        request.setStatus(RequestStatus.REJECTED);
        TenantRequest saved = tenantRequestRepository.save(request);

        // Notify the tenant
        notificationService.create(
                request.getTenant(),
                "Your request for \"" + request.getProperty().getTitle() + "\" was declined.",
                "REQUEST_REJECTED",
                request.getProperty().getId(),
                saved.getId()
        );

        return saved;
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

    private TenantRequest getRequest(Long id) {
        return tenantRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Request not found"));
    }

    private void verifyOwner(TenantRequest request, String ownerEmail) {
        if (!request.getProperty().getOwner().getEmail().equals(ownerEmail)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You are not the owner of this property");
        }
    }
}