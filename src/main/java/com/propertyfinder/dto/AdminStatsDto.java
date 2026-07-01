package com.propertyfinder.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminStatsDto {

    private long totalUsers;
    private long totalOwners;
    private long totalTenants;
    private long totalProperties;
    private long totalRequests;
}