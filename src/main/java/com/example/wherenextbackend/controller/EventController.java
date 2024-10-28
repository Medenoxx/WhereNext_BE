package com.example.wherenextbackend.controller;

import com.example.wherenextbackend.dto.*;
import com.example.wherenextbackend.entity.Event;
import com.example.wherenextbackend.entity.User;
import com.example.wherenextbackend.services.CommentService;
import com.example.wherenextbackend.services.EventService;
import com.example.wherenextbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final UserService userService;
    private final CommentService commentService;

    @PostMapping()
    public ResponseEntity<CreateEventResponseDTO> addEvent(@RequestBody EventRequestDTO eventRequestDTO, Authentication authentication) {
        CreateEventResponseDTO response = eventService.addEvent(eventRequestDTO, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Endpunkt zum Abrufen aller Events
    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents(Authentication authentication) {
        List<EventResponseDTO> events = eventService.getAllEvents(authentication);
        return ResponseEntity.ok(events);
    }

    // Endpunkt, um ein Event anhand der ID abzurufen
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Integer id, Authentication authentication) {
        EventResponseDTO responseDTO = eventService.getEventById(id, authentication);
        if (responseDTO != null) {
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpunkt, um einem Event beizutreten
    @PostMapping("/{eventId}/join")
    public ResponseEntity<String> joinEvent(@PathVariable Integer eventId, @RequestBody EventJoinRequestDTO eventJoinRequestDTO) {
        try {
            // Nutzer aus der Authentifizierung bekommen
            User user = userService.getUserById(eventJoinRequestDTO.getParticipantId());

            boolean joined = eventService.joinEvent(eventId, user, eventJoinRequestDTO.getAccessCode());


            if (joined) {
                return ResponseEntity.ok("Successfully joined the event");
            } else {
                // Prüfe, warum der Nutzer nicht teilnehmen konnte
                if (eventService.isUserAlreadyParticipant(eventId, user)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("User already joined the event");
                } else {
                    return ResponseEntity.badRequest().body("Invalid access code or unable to join event");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to join event");
        }
    }


    // Endpunkt, um ein Event zu verlassen
    @PostMapping("/{id}/leave")
    public ResponseEntity<String> leaveEvent(@PathVariable Integer id, Authentication authentication) {
        try {
            // Nutzer aus der Authentifizierung bekommen
            User user = userService.getUserByAuthentication(authentication);
            boolean left = eventService.leaveEvent(id, user);

            if (left) {
                return ResponseEntity.ok("Successfully left the event");
            } else {
                return ResponseEntity.badRequest().body("Unable to leave event");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to leave event");
        }
    }

    @GetMapping("/created")
    public ResponseEntity<List<EventResponseDTO>> getEventsCreatedByUser(Authentication authentication) {
        List<EventResponseDTO> events = eventService.getEventsCreatedByUser(authentication);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/participated")
    public ResponseEntity<List<EventResponseDTO>> getEventsParticipatedByUser(Authentication authentication) {
        List<EventResponseDTO> events = eventService.getEventsParticipatedByUser(authentication);
        return ResponseEntity.ok(events);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Integer eventId, Authentication authentication) {
        boolean deleted = eventService.deleteEvent(eventId, authentication);
        if (deleted) {
            return ResponseEntity.ok("Event deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No permission to delete this event.");
        }
    }

    @PostMapping("/{eventId}/comments")
    public ResponseEntity<CommentResponseDTO> addComment(@RequestBody CommentRequestDTO commentRequestDTO) {
        CommentResponseDTO response = commentService.addComment(commentRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{eventId}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getAllCommentsForEvent(@PathVariable Integer eventId) {
        Event currentEvent = eventService.getEventEntityById(eventId);
        List<CommentResponseDTO> comments = commentService.getAllCommentsForEvent(currentEvent);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @DeleteMapping("/{eventId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId, @PathVariable Integer eventId, Authentication authentication) {
        commentService.deleteComment(commentId, eventId, authentication);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
