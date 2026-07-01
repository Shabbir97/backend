package com.propertyfinder.specification;

import com.propertyfinder.dto.PropertyFilter;
import com.propertyfinder.entity.Property;
import org.springframework.data.jpa.domain.Specification;

public class PropertySpecification {

    public static Specification<Property> filter(
            PropertyFilter filter
    ) {

        return (root, query, cb) -> {

            var predicate = cb.conjunction();

            if (filter.getLocation() != null &&
                    !filter.getLocation().isBlank()) {

                predicate = cb.and(
                        predicate,
                        cb.like(
                                cb.lower(root.get("location")),
                                "%" + filter.getLocation().toLowerCase() + "%"
                        )
                );
            }

            if (filter.getMaxPrice() != null) {

                predicate = cb.and(
                        predicate,
                        cb.lessThanOrEqualTo(
                                root.get("price"),
                                filter.getMaxPrice()
                        )
                );
            }

            if (filter.getBedrooms() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("bedrooms"),
                                filter.getBedrooms()
                        )
                );
            }

            if (filter.getBathrooms() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("bathrooms"),
                                filter.getBathrooms()
                        )
                );
            }

            if (filter.getAvailable() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("available"),
                                filter.getAvailable()
                        )
                );
            }

            return predicate;
        };
    }
}