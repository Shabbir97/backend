package com.propertyfinder.dto;

import lombok.Data;

@Data
public class PropertyFilter {

    private String location;

    private Double maxPrice;

    private Integer bedrooms;

    private Integer bathrooms;

    private Boolean available;

    private int page = 0;

    private int size = 10;
}