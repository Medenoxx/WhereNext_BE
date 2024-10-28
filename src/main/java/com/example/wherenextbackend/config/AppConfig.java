package com.example.wherenextbackend.config;

import com.example.wherenextbackend.repository.UserCrudRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UserCrudRepo userCrudRepo;

    //Beschreibt womit authentifiziert wird in diesem Fall soll das UserdetailService verwendet werden und der BCryptpasswordEncoder
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    //Das ist der Punkt an dem festgelegt wird nach welchen Properties der User geladen wird.
    //Sprich hier wird festgelegt womit man sich einloggen kann
    //In diesem Beispiel mit username
    @Bean
    UserDetailsService userDetailsService() {
        return username -> userCrudRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Benutzer existiert nicht"));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}