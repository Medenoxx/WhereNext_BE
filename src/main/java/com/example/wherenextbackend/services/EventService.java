package com.example.wherenextbackend.services;

import com.example.wherenextbackend.dto.EventRequestDTO;
import com.example.wherenextbackend.dto.CreateEventResponseDTO;
import com.example.wherenextbackend.dto.EventResponseDTO;
import com.example.wherenextbackend.entity.Category;
import com.example.wherenextbackend.entity.Event;
import com.example.wherenextbackend.entity.User;
import com.example.wherenextbackend.repository.EventCrudRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventCrudRepo eventCrudRepo;
    private final UserService userService;
    private final CategoryService categoryService;


    public CreateEventResponseDTO convertEventToCreateEventResponseDTO(Event event) {
        return CreateEventResponseDTO.builder().eventId(event.getEventId()).organisatorId(event.getOrganisator().getUserId()).name(event.getName()).eventDescription(event.getEventDescription()).categoryId(event.getCategory().getCategoryId()).date(event.getDate()).isPrivate(event.getIsPrivate()).maxParticipants(event.getMaxParticipants()).latitude(event.getLatitude()).longitude(event.getLongitude()).location(event.getLocation()).accessCode(event.getIsPrivate() ? event.getAccessCode() : null)// Zugangscode nur bei privaten Events
                .isOutdoor(event.getIsOutdoor()).build();

    }

    public EventResponseDTO convertEventToEventResponseDTO(Event event, Boolean isParticipant) {
        return new EventResponseDTO(event, isParticipant);
    }

    public CreateEventResponseDTO addEvent(EventRequestDTO eventRequestDTO, Authentication authentication) {
        User user = userService.getUserByAuthentication(authentication);
        Category category = categoryService.getCategoryById(eventRequestDTO.getCategoryId());
        System.out.println("Creating event with isOutdoor: " + eventRequestDTO.getIsOutdoor());
        Event event = Event.builder().name(eventRequestDTO.getName()).eventDescription(eventRequestDTO.getDescription()).date(eventRequestDTO.getDate()).time(eventRequestDTO.getTime()).isPrivate(eventRequestDTO.getIsPrivate()).maxParticipants(eventRequestDTO.getMaxParticipants()).enabled(true).latitude(eventRequestDTO.getLatitude()).longitude(eventRequestDTO.getLongitude()).location(eventRequestDTO.getLocation()).organisator(user).category(category).participants(new ArrayList<>()).isOutdoor(eventRequestDTO.getIsOutdoor()).build();
        if (event.getIsPrivate()) {
            String accessCode = generateAccessCode();
            event.setAccessCode(accessCode);
        }
        eventCrudRepo.save(event);
        return convertEventToCreateEventResponseDTO(event);
    }

    private String generateAccessCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    public List<EventResponseDTO> getAllEvents(Authentication authentication) {
        List<Event> events = eventCrudRepo.findAll();
        List<EventResponseDTO> eventResponseDTOS = new ArrayList<>();
        User currentUser = null;

        if (authentication != null) {
            currentUser = userService.getUserByAuthentication(authentication);
        }

        for (Event event : events) {
            boolean isParticipant = currentUser != null && event.getParticipants().contains(currentUser);
            eventResponseDTOS.add(new EventResponseDTO(event, isParticipant));
        }
        return eventResponseDTOS;
    }

    // Methode, um ein Event anhand der ID zu erhalten
    public EventResponseDTO getEventById(Integer id, Authentication authentication) {
        Optional<Event> eventOpt = eventCrudRepo.findById(id);
        if (eventOpt.isPresent()) {
            Event event = eventOpt.get();

            // Get current user
            User currentUser = userService.getUserByAuthentication(authentication);

            // Check if user is a participant or organisator
            boolean isParticipant = event.getParticipants().contains(currentUser);
            boolean isOrganisator = event.getOrganisator().equals(currentUser);
            EventResponseDTO responseDTO = new EventResponseDTO(event, isParticipant);

            // Nur der Organisator darf den Zugangscode sehen
            if (isOrganisator) {
                responseDTO.setAccessCode(event.getAccessCode());
            }

            return responseDTO;
        } else {
            return null;
        }
    }


    public Event getEventEntityById(Integer eventId) {
        return eventCrudRepo.findById(eventId).get();
    }

    // Methode, um einem Event beizutreten
    public boolean joinEvent(Integer eventId, User user, String providedAccessCode) {
        Optional<Event> eventOpt = eventCrudRepo.findById(eventId);
        if (eventOpt.isEmpty()) {
            System.out.println("Event not found");
            return false; // Event existiert nicht
        }

        Event event = eventOpt.get();

        if (event.getIsPrivate() && !event.getAccessCode().equals(providedAccessCode)) {
            System.out.println("Provided Access Code does not match with Event Access Code");
            return false;
        }

        // Prüfe, ob das Event voll ist
        if (event.getParticipants().size() >= event.getMaxParticipants()) {
            System.out.println("Event is already full");
            return false; // Das Event ist bereits voll
        }

        // Prüfe, ob der Benutzer bereits Teilnehmer ist
        if (event.getParticipants().contains(user)) {
            System.out.println("User already joined the event");
            return false; // Benutzer ist bereits Teilnehmer
        }

        // Füge den Nutzer als Teilnehmer hinzu
        event.getParticipants().add(user);
        eventCrudRepo.save(event);
        return true;
    }


    // Methode, um ein Event zu verlassen
    public boolean leaveEvent(Integer eventId, User user) {
        Optional<Event> eventOpt = eventCrudRepo.findById(eventId);

        if (eventOpt.isPresent()) {
            Event event = eventOpt.get();

            // Überprüfe, ob der Nutzer bereits dem Event beigetreten ist
            if (event.getParticipants().contains(user)) {
                event.getParticipants().remove(user);
                eventCrudRepo.save(event);
                return true;
            }
        }
        return false;
    }

    public boolean isUserAlreadyParticipant(Integer eventId, User user) {
        Optional<Event> eventOpt = eventCrudRepo.findById(eventId);
        if (eventOpt.isEmpty()) {
            return false; // Event existiert nicht
        }

        Event event = eventOpt.get();
        return event.getParticipants().contains(user); // Überprüfe, ob der User bereits in der Teilnehmerliste ist
    }

    public List<EventResponseDTO> getEventsCreatedByUser(Authentication authentication) {
        User currentUser = userService.getUserByAuthentication(authentication);
        List<Event> events = eventCrudRepo.findByOrganisator(currentUser);
        return events.stream().map(event -> new EventResponseDTO(event, true)).collect(Collectors.toList());
    }

    public List<EventResponseDTO> getEventsParticipatedByUser(Authentication authentication) {
        User currentUser = userService.getUserByAuthentication(authentication);
        List<Event> events = eventCrudRepo.findByParticipantsContains(currentUser);
        return events.stream().map(event -> new EventResponseDTO(event, true)).collect(Collectors.toList());
    }

    public boolean deleteEvent(Integer eventId, Authentication authentication) {
        Optional<Event> eventOpt = eventCrudRepo.findById(eventId);
        if (eventOpt.isPresent()) {
            Event event = eventOpt.get();
            User currentUser = userService.getUserByAuthentication(authentication);
            if (event.getOrganisator().equals(currentUser)) {
                eventCrudRepo.delete(event);
                return true;
            }
        }
        return false;
    }

}


