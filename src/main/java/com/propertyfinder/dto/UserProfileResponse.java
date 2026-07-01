package com.propertyfinder.dto;

import com.propertyfinder.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {

    private Long id;

    private String fullName;

    private String email;

    private String phone;

    private Role role;
}