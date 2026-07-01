package com.propertyfinder.service;

import com.propertyfinder.specification.PropertySpecification;
import com.propertyfinder.enums.Role; 
import com.propertyfinder.dto.PropertyFilter;
import com.propertyfinder.dto.PropertyRequest;
import com.propertyfinder.entity.Property;
import com.propertyfinder.entity.User;
import com.propertyfinder.repository.PropertyRepository;
import com.propertyfinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    // CREATE PROPERTY
    public Property createProperty(PropertyRequest request, String ownerEmail) {

        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (owner.getRole() == Role.OWNER
                && !owner.isApproved()) {

            throw new RuntimeException(
                    "Owner account not approved yet");
        }

        Property property = Property.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .price(request.getPrice())
                .bedrooms(request.getBedrooms())
                .bathrooms(request.getBathrooms())
                .type(request.getType())
                .available(true)
                .owner(owner)
                .build();

        return propertyRepository.save(property);
    }

    // GET ALL
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    // GET PROPERTY BY ID
    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
    }

    // FILTER + PAGINATION
    public Page<Property> getFilteredProperties(
            PropertyFilter filter
    ) {

        Pageable pageable =
                PageRequest.of(
                        filter.getPage(),
                        filter.getSize()
                );

        return propertyRepository.findAll(
                PropertySpecification.filter(filter),
                pageable
        );
    }

    // UPLOAD PROPERTY IMAGE
    public Property uploadPropertyImage(
            Long propertyId,
            MultipartFile file,
            String ownerEmail
    ) throws IOException {

        Property property =
                propertyRepository.findById(propertyId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Property not found"));

        if (!property.getOwner()
                .getEmail()
                .equals(ownerEmail)) {

            throw new RuntimeException(
                    "Not allowed"
            );
        }

        String imagePath =
                fileStorageService.uploadImage(file);

        property.setImageUrl(imagePath);

        return propertyRepository.save(property);
    }
    public Property updateProperty(
            Long propertyId,
            PropertyRequest request,
            String ownerEmail
    ) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() ->
                        new RuntimeException("Property not found"));

        // SECURITY CHECK
        if (!property.getOwner().getEmail().equals(ownerEmail)) {
            throw new RuntimeException(
                    "You are not allowed to update this property"
            );
        }

        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setLocation(request.getLocation());
        property.setPrice(request.getPrice());
        property.setBedrooms(request.getBedrooms());
        property.setBathrooms(request.getBathrooms());

        return propertyRepository.save(property);
    }

    public String deleteProperty(
            Long propertyId,
            String ownerEmail
    ) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() ->
                        new RuntimeException("Property not found"));

        // SECURITY CHECK
        if (!property.getOwner().getEmail().equals(ownerEmail)) {
            throw new RuntimeException(
                    "You are not allowed to delete this property"
            );
        }

        propertyRepository.delete(property);

        return "Property deleted successfully";
    }

    public List<Property> getMyProperties(
            String ownerEmail
    ) {

        return propertyRepository.findByOwnerEmail(
                ownerEmail
        );
    }
}