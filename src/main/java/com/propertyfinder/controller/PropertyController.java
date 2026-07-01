package com.propertyfinder.controller;

import com.propertyfinder.dto.PropertyFilter;
import com.propertyfinder.dto.PropertyRequest;
import com.propertyfinder.entity.Property;
import com.propertyfinder.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    // CREATE PROPERTY
    @PostMapping
    public Property createProperty(
            @Valid @RequestBody PropertyRequest request,
            Authentication authentication
    ) {

        return propertyService.createProperty(
                request,
                authentication.getName()
        );
    }

    // GET ALL PROPERTIES
    @GetMapping
    public List<Property> getAllProperties() {
        return propertyService.getAllProperties();
    }

    // GET PROPERTY BY ID
    @GetMapping("/{id}")
    public Property getPropertyById(
            @PathVariable Long id
    ) {
        return propertyService.getPropertyById(id);
    }

    // UPDATE PROPERTY
    @PutMapping("/{id}")
    public Property updateProperty(
            @PathVariable Long id,
            @RequestBody PropertyRequest request,
            Authentication authentication
    ) {

        return propertyService.updateProperty(
                id,
                request,
                authentication.getName()
        );
    }

    // DELETE PROPERTY
    @DeleteMapping("/{id}")
    public String deleteProperty(
            @PathVariable Long id,
            Authentication authentication
    ) {

        return propertyService.deleteProperty(
                id,
                authentication.getName()
        );
    }

    // OWNER'S PROPERTIES
    @GetMapping("/mine")
    public List<Property> getMyProperties(
            Authentication authentication
    ) {

        return propertyService.getMyProperties(
                authentication.getName()
        );
    }

    // FILTER + PAGINATION
    @PostMapping("/filter")
    public Page<Property> filterProperties(
            @RequestBody PropertyFilter filter
    ) {

        return propertyService.getFilteredProperties(filter);
    }

    // UPLOAD IMAGE
    @PostMapping("/{id}/image")
    public Property uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) throws IOException {

        return propertyService.uploadPropertyImage(
                id,
                file,
                authentication.getName()
        );
    }

}