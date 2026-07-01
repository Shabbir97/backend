package com.propertyfinder.dto;

import com.propertyfinder.enums.PropertyType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PropertyRequest {

    private PropertyType type;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @Positive(message = "Price must be positive")
    private Double price;

    @Min(value = 1, message = "Bedrooms must be at least 1")
    private Integer bedrooms;

    @Min(value = 1, message = "Bathrooms must be at least 1")
    private Integer bathrooms;
}