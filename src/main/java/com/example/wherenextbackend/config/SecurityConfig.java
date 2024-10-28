package com.example.wherenextbackend.config;

import com.example.wherenextbackend.components.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;


    private final AuthenticationProvider authenticationProvider;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // Wenn nötig
        config.addAllowedOrigin("http://localhost:5173"); // Füge deine Frontend-URL hinzu
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        return http
                .cors(Customizer.withDefaults()) // Aktiviert CORS
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("api/auth/login").permitAll()
                        .requestMatchers("api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET,"api/events").authenticated() //event anschauen: alle
                        .requestMatchers(HttpMethod.POST,"api/events/list").authenticated() //event erstellen: nur eingeloggte user
                        .requestMatchers(HttpMethod.GET,"api/users").authenticated()
                        .requestMatchers(HttpMethod.PUT,"api/users").authenticated()
                        .requestMatchers(HttpMethod.POST, "api/events/{eventId}/join").authenticated() // Join-Event Endpoint
                        .requestMatchers(HttpMethod.POST, "api/events/{eventId}/comments").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/comments/*/admin").hasAuthority("ADMIN")
                        // Zugriff auf andere Kommentare-Endpunkte
                        .requestMatchers(HttpMethod.GET, "/api/comments").hasAuthority("ADMIN")
                        //nur wenn eingeloggt user laden schreiben api/auth
                        //später mit anderen requests vervollständigen, zB event kreieren -> nur wenn eingeloggt und rolle admin etc...
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .build();
    }
}

