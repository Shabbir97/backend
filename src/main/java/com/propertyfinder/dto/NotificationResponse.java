package com.propertyfinder.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private Long id;
    private String message;
    private String type;
    private boolean read;
    private Long relatedPropertyId;
    private Long relatedRequestId;
    private LocalDateTime createdAt;
}