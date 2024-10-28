package com.example.wherenextbackend.repository;

import com.example.wherenextbackend.entity.Event;  // Ensure correct Event import
import com.example.wherenextbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EventCrudRepo extends JpaRepository<Event, Integer> {

    // Methode zum Finden von Events, die von einem bestimmten Organisator erstellt wurden
    List<Event> findByOrganisator(User organisator);

    // Methode zum Finden von Events, an denen ein bestimmter Benutzer teilnimmt
    List<Event> findByParticipantsContains(User participant);


}

