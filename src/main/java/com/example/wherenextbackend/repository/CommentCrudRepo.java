package com.example.wherenextbackend.repository;

import com.example.wherenextbackend.entity.Comment;
import com.example.wherenextbackend.entity.Event;
import com.example.wherenextbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentCrudRepo extends JpaRepository<Comment, Integer> {

    List<Comment> findByUser(User user);

    List<Comment> findByEvent(Event event);
}