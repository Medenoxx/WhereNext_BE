package com.example.wherenextbackend.services;

import com.example.wherenextbackend.customException.EmptyOptionalException;
import com.example.wherenextbackend.customException.PrincipalFormatException;
import com.example.wherenextbackend.dto.*;
import com.example.wherenextbackend.entity.User;
import com.example.wherenextbackend.repository.UserCrudRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserCrudRepo userCrudRepo;
    private final ConversionService conversionService;


    public User getUserById(Integer userId) {
        return getUserFromOptional(userCrudRepo.findById(userId));
    }

    public UserResponseDTO getOneUserById(Integer userId) {
        User user = getUserById(userId);
        return convertUserToUserResponseDTO(user);
    }

    public UserResponseDTO convertUserToUserResponseDTO(User user) {
        return new UserResponseDTO(user.getUserId(), user.getUsername(), user.getEmail(), user.getLastName(), user.getFirstName(), user.getRole());
    }

    public User getUserByUsername(String username) {
        return getUserFromOptional(userCrudRepo.findByUsername(username));
    }

    public User getUserByAuthentication(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof User user)) {
            throw new PrincipalFormatException("Principal should be User");
        }
        return getUserById(user.getUserId());
    }

    public User getUserFromOptional(Optional<User> userOptional) {
        User user;
        try {
            user = conversionService.getEntityFromOptional(userOptional);
        } catch (EmptyOptionalException e) {
            throw new UsernameNotFoundException("Kein entsprechender User in der Datenbank gefunden!");
        }
        return user;
    }

    public void saveUser(User user) {
        userCrudRepo.save(user);
    }

    public List<UserRequestDTO> getAllUsers() {
        List<User> users = userCrudRepo.findAll();
        List<UserRequestDTO> result = new ArrayList<>();
        for (User user : users) {
            result.add(new UserRequestDTO(user.getUsername(), user.getEmail(), user.getLastName(), user.getFirstName()));
        }
        return result;
    }

    public User updateUser(Integer userId, UserDetailsRequestDTO userDetailsRequestDTO) {
        User userToUpdate = getUserById(userId);
        userToUpdate.setFirstName(userDetailsRequestDTO.getFirstName());
        userToUpdate.setLastName(userDetailsRequestDTO.getLastName());
        userToUpdate.setAboutMe(userDetailsRequestDTO.getAboutMe());
        String hobbiesList = String.join(";", userDetailsRequestDTO.getHobbies());
        userToUpdate.setHobbies(hobbiesList);
        String interestsList = String.join(";", userDetailsRequestDTO.getInterests());
        userToUpdate.setInterests(interestsList);

        return userCrudRepo.save(userToUpdate);

    }

    public UserDetailsResponseDTO getUserDetails(Integer userId) {
        User userToLoad = getUserById(userId);
        String[] hobbiesArray = userToLoad.getHobbies() != null ? userToLoad.getHobbies().split(";") : new String[0];
        String[] interestsArray = userToLoad.getInterests() != null ? userToLoad.getInterests().split(";") : new String[0];
        return new UserDetailsResponseDTO(userToLoad.getUserId(), userToLoad.getUsername(), userToLoad.getEmail(), userToLoad.getFirstName(), userToLoad.getLastName(), userToLoad.getAboutMe(), userToLoad.getAvatarUrl(), hobbiesArray, interestsArray);
    }
}



