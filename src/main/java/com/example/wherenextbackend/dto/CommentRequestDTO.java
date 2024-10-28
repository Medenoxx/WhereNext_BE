package com.example.wherenextbackend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class CommentRequestDTO {
    private Integer userId;
    private Integer eventId;
    private String message;
    private String commentTimestamp;
}
