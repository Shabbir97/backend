package com.propertyfinder.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {

    private String fullName;

    private String phone;
}