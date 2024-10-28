package com.example.wherenextbackend.entity;

import com.example.wherenextbackend.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "Users")

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Column
    private String aboutMe;

    @Column
    private String hobbies;

    @Column
    private String interests;

    @Column
    private String avatarUrl;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private boolean enabled;

    //welche events von user organisiert wurden
    @OneToMany(mappedBy = "organisator")
    private List<Event> organizedEventsList = new ArrayList<>();

    @ManyToMany(mappedBy = "participants")
    private List<Event> eventList = new ArrayList<>();

    //Beziehung zu Comment
    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    //Konstruktor ohne optionale Daten
    public User(String username, String email, String firstName, String lastName, String password, UserRole userRole, String avatarUrl) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.role = userRole;
        this.avatarUrl = avatarUrl;
        this.enabled = true;
    }

    //Konstruktor allg.
    public User(String username, String email, String firstName, String lastName, String password, String aboutMe, String hobbies, String interests, UserRole userRole, String avatarUrl) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.aboutMe = aboutMe;
        this.hobbies = hobbies;
        this.interests = interests;
        this.role = userRole;
        this.avatarUrl = avatarUrl;
        this.enabled = true;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(role.toString()));
        return authorityList;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
//