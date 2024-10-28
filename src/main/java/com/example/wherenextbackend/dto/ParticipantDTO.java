package com.example.wherenextbackend.dto;

import com.example.wherenextbackend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ParticipantDTO {
    private Integer userId;
    private String username;
    private String avatarUrl;

    public ParticipantDTO(User user) {
        if (user != null) {
            this.userId = user.getUserId();
            this.username = user.getUsername();
            this.avatarUrl = user.getAvatarUrl();
        }
    }
}



