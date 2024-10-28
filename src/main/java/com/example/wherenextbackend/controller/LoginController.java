package com.example.wherenextbackend.controller;

import com.example.wherenextbackend.dto.AuthRegistrationLoginResponseDTO;
import com.example.wherenextbackend.dto.LoginRequestDTO;
import com.example.wherenextbackend.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth/")
public class LoginController {


    private final AuthService authService;


    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        AuthRegistrationLoginResponseDTO authRegistrationLoginResponseDTO;

        try {
            authRegistrationLoginResponseDTO = authService.login(loginRequestDTO);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(authRegistrationLoginResponseDTO, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<AuthRegistrationLoginResponseDTO> loadUser(Authentication authentication) {
        AuthRegistrationLoginResponseDTO userDTO = authService.loadUser(authentication);
        return ResponseEntity.ok(userDTO);
    }
}