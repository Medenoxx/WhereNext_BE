package com.example.wherenextbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "Event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventId;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String eventDescription;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String time;

    @Column(nullable = false)
    private Boolean isPrivate;

    @Column(nullable = false)
    private Integer maxParticipants;

    @Column(nullable = true)
    private String accessCode;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Boolean isOutdoor;

    //Beziehung welcher User ein Event erstellt hat
    @ManyToOne
    @JoinColumn(name = "organisatorId")
    private User organisator;

    @ManyToMany
    @JoinTable(name = "UserInEvent", joinColumns = @JoinColumn(name = "eventId"), inverseJoinColumns = @JoinColumn(name = "userId"))
    private List<User> participants = new ArrayList<>();


    //Beziehung zu Category
    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    //Beziehung zu Comment
    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


}
