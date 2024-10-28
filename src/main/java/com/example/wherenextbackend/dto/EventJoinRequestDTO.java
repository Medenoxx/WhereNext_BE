package com.example.wherenextbackend.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventJoinRequestDTO {
    private Integer participantId;
    private String accessCode;
}


