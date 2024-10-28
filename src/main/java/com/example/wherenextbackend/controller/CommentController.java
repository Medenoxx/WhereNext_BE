package com.example.wherenextbackend.controller;

import com.example.wherenextbackend.dto.CommentResponseDTO;
import com.example.wherenextbackend.entity.Comment;
import com.example.wherenextbackend.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> getAllComments() {
        // Ruft alle Kommentare als DTOs ab
        List<CommentResponseDTO> comments = commentService.getAllComments();
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}/admin")
    public ResponseEntity<Void> deleteCommentAsAdmin(@PathVariable Integer commentId, Authentication authentication) {
        System.out.println("Received DELETE request for comment with ID: " + commentId);

        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
            commentService.deleteCommentAsAdmin(commentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Erfolgreich gelöscht
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Keine Berechtigung
    }


}

