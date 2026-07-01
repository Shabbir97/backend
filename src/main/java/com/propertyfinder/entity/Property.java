package com.propertyfinder.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.propertyfinder.enums.PropertyType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "property")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String location;
    private double price;
    private int bedrooms;
    private int bathrooms;
    private boolean available;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private PropertyType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    @JsonIgnoreProperties({"password", "hibernateLazyInitializer", "handler"})
    private User owner;
}