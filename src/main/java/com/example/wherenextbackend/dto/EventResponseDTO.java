package com.example.wherenextbackend.dto;

import com.example.wherenextbackend.entity.Event;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDTO {
    private Integer eventId;
    private Integer organisatorId;
    private String name;
    private String eventDescription;
    private Integer categoryId;
    private LocalDate date;
    private String time;
    private Boolean isPrivate;
    private Integer maxParticipants;
    private String accessCode;
    private Double latitude;
    private Double longitude;
    private String location;
    private List<ParticipantDTO> participantsInEvent;
    private Boolean isParticipant;
    private Boolean isOutdoor;


    public EventResponseDTO(Event event) {
        if (event != null) {
            this.eventId = event.getEventId();
            this.organisatorId = event.getOrganisator() != null ? event.getOrganisator().getUserId() : null;
            this.name = event.getName();
            this.eventDescription = event.getEventDescription();
            this.categoryId = event.getCategory() != null ? event.getCategory().getCategoryId() : null;
            this.date = event.getDate();
            this.time = event.getTime();
            this.isPrivate = event.getIsPrivate();
            this.maxParticipants = event.getMaxParticipants();
            this.accessCode = event.getAccessCode();
            this.latitude = event.getLatitude();
            this.longitude = event.getLongitude();
            this.location = event.getLocation();
            this.participantsInEvent = event.getParticipants() != null ? event.getParticipants().stream()
                    .map(ParticipantDTO::new)
                    .collect(Collectors.toList()) : null;
            this.isParticipant = false; // Default value
            this.isOutdoor = event.getIsOutdoor();
        }
    }


    public EventResponseDTO(Event event, Boolean isParticipant) {
        this(event);
        this.isParticipant = isParticipant;
    }
}
