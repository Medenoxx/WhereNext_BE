package com.example.wherenextbackend.services;

import com.example.wherenextbackend.customException.UserAlreadyExistsException;
import com.example.wherenextbackend.dto.AuthRegistrationLoginResponseDTO;
import com.example.wherenextbackend.dto.LoginRequestDTO;
import com.example.wherenextbackend.dto.RegistrationRequestDTO;
import com.example.wherenextbackend.entity.User;
import com.example.wherenextbackend.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    //post
    public AuthRegistrationLoginResponseDTO register(RegistrationRequestDTO registrationRequestDTO) throws UserAlreadyExistsException {
        UserRole role = registrationRequestDTO.getEmail().equals("admin@example.com") ? UserRole.ADMIN : UserRole.USER;

        User user = new User(registrationRequestDTO.getUsername(), registrationRequestDTO.getEmail(), registrationRequestDTO.getFirstName(), registrationRequestDTO.getLastName(), passwordEncoder.encode(registrationRequestDTO.getPassword()), role, registrationRequestDTO.getAvatarUrl());

        userService.saveUser(user);

        String jwt = tokenService.generateTokenWithClaims(user);

        return new AuthRegistrationLoginResponseDTO(user.getUserId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getRole().getLabel(), jwt);
    }

    //get von eigenem User
    public AuthRegistrationLoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        User user = userService.getUserByUsername(loginRequestDTO.getUsername());
        String username = user.getUsername();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, loginRequestDTO.getPassword()));

        String jwt = tokenService.generateTokenWithClaims(user);

        return new AuthRegistrationLoginResponseDTO(user.getUserId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getRole().getLabel(), jwt);
    }

    public AuthRegistrationLoginResponseDTO loadUser(Authentication authentication) {
        User user = userService.getUserByAuthentication(authentication);
        String jwt = tokenService.generateTokenWithClaims(user);

        return new AuthRegistrationLoginResponseDTO(user.getUserId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getRole().getLabel(), jwt);
    }

}




