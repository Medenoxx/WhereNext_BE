package com.example.wherenextbackend.controller;

import com.example.wherenextbackend.customException.UserAlreadyExistsException;
import com.example.wherenextbackend.dto.AuthRegistrationLoginResponseDTO;
import com.example.wherenextbackend.dto.RegistrationRequestDTO;
import com.example.wherenextbackend.dto.UserResponseDTO;
import com.example.wherenextbackend.entity.User;
import com.example.wherenextbackend.services.AuthService;
import com.example.wherenextbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/auth/")
@RequiredArgsConstructor

public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequestDTO registrationRequestDTO) {

        AuthRegistrationLoginResponseDTO authRegistrationLoginResponseDTO;

        try {
            authRegistrationLoginResponseDTO = authService.register(registrationRequestDTO);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(authRegistrationLoginResponseDTO, HttpStatus.CREATED);
    }

}