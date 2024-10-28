package com.example.wherenextbackend.dto;

import java.time.LocalDate;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateEventResponseDTO {

    private Integer eventId;
    private Integer organisatorId;
    private String name;
    private String eventDescription;
    private Integer categoryId;
    private LocalDate date;
    private Boolean isPrivate;
    private Integer maxParticipants;
    private String accessCode;
    private Double latitude;
    private Double longitude;
    private String location;
    private Boolean isOutdoor;

}
