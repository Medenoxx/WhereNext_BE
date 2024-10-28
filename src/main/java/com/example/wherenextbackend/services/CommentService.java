package com.example.wherenextbackend.services;

import com.example.wherenextbackend.customException.ResourceNotFoundException;
import com.example.wherenextbackend.dto.CommentRequestDTO;
import com.example.wherenextbackend.dto.CommentResponseDTO;
import com.example.wherenextbackend.entity.Comment;
import com.example.wherenextbackend.entity.Event;
import com.example.wherenextbackend.entity.User;
import com.example.wherenextbackend.repository.CommentCrudRepo;
import com.example.wherenextbackend.repository.EventCrudRepo;
import com.example.wherenextbackend.repository.UserCrudRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentCrudRepo commentCrudRepo;
    private final UserCrudRepo userCrudRepo;
    private final EventCrudRepo eventCrudRepo;

    private final UserService userService;

    public CommentResponseDTO addComment(CommentRequestDTO commentRequestDTO) {
        //System.out.println("UserID: " + commentRequestDTO.getUserId()+ ", EventID: " + commentRequestDTO.getEventId()+ commentRequestDTO.getMessage() + commentRequestDTO.getCommentTimestamp());
        User user = userCrudRepo.findById(commentRequestDTO.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Event event = eventCrudRepo.findById(commentRequestDTO.getEventId()).orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setEvent(event);
        comment.setMessage(commentRequestDTO.getMessage());
        comment.setCommentTimestamp(commentRequestDTO.getCommentTimestamp());

        Comment savedComment = commentCrudRepo.save(comment);
        return new CommentResponseDTO(savedComment.getCommentId(), savedComment.getUser().getUserId(), savedComment.getEvent().getEventId(), savedComment.getMessage(), savedComment.getCommentTimestamp());
    }

    public List<CommentResponseDTO> getAllCommentsForEvent(Event event) {
        List<Comment> comments = commentCrudRepo.findByEvent(event);
        List<CommentResponseDTO> commentResponseDTOs = new ArrayList<>();

        for (Comment comment : comments) {
            CommentResponseDTO commentResponseDTO = new CommentResponseDTO(comment.getCommentId(), comment.getUser().getUserId(), comment.getEvent().getEventId(), comment.getMessage(), comment.getCommentTimestamp());
            commentResponseDTOs.add(commentResponseDTO);
        }

        return commentResponseDTOs;
    }

    public List<CommentResponseDTO> getAllCommentsFromUser(User user) {
        List<Comment> comments = commentCrudRepo.findByUser(user);
        List<CommentResponseDTO> commentResponseDTOs = new ArrayList<>();

        for (Comment comment : comments) {
            CommentResponseDTO commentResponseDTO = new CommentResponseDTO(comment.getCommentId(), comment.getUser().getUserId(), comment.getEvent().getEventId(), comment.getMessage(), comment.getCommentTimestamp());
            commentResponseDTOs.add(commentResponseDTO);
        }

        return commentResponseDTOs;
    }

    public void deleteComment(Integer commentId, Integer eventId, Authentication authentication) {
        if (!commentCrudRepo.existsById(commentId)) {
            throw new ResourceNotFoundException("Comment with id " + commentId + " not found");
        }

        Optional<Comment> commentOpt = commentCrudRepo.findById(commentId);
        Optional<Event> eventOpt = eventCrudRepo.findById(eventId);
        if (eventOpt.isPresent()) {
            Event event = eventOpt.get();
            User currentUser = userService.getUserByAuthentication(authentication);
            User commentUser = commentOpt.get().getUser();
            if (event.getOrganisator().equals(currentUser) || commentUser.equals(currentUser)) {
                commentCrudRepo.deleteById(commentId);
            }
        }

    }

    public List<CommentResponseDTO> getAllComments() {
        // Abrufen aller Kommentare und Umwandlung in eine Liste von CommentResponseDTO
        List<Comment> comments = commentCrudRepo.findAll();
        return comments.stream().map(comment -> new CommentResponseDTO(comment.getCommentId(), comment.getUser().getUserId(), comment.getEvent().getEventId(), comment.getMessage(), comment.getCommentTimestamp())).collect(Collectors.toList());
    }

    public void deleteCommentAsAdmin(Integer commentId) {
        Optional<Comment> commentOptional = commentCrudRepo.findById(commentId);

        if (commentOptional.isPresent()) {
            System.out.println("Deleting comment with ID: " + commentId);
            commentCrudRepo.delete(commentOptional.get());
        } else {
            System.out.println("Comment with ID " + commentId + " does not exist");
            throw new IllegalArgumentException("Comment with ID " + commentId + " does not exist");
        }
    }

}

