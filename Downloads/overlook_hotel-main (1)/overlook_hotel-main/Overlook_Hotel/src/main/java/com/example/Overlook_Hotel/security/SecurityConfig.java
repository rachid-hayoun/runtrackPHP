package com.example.Overlook_Hotel.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                         CustomUserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // Endpoints publics
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/chambres").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/chambres/disponibles").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/chambres/type/**").permitAll()
                .requestMatchers("/api/clients/register").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                
                // Endpoints clients
                .requestMatchers(HttpMethod.POST, "/api/reservations").hasRole("CLIENT")
                .requestMatchers(HttpMethod.GET, "/api/reservations/client/**").hasRole("CLIENT")
                .requestMatchers(HttpMethod.POST, "/api/feedbacks").hasRole("CLIENT")
                .requestMatchers(HttpMethod.PUT, "/api/clients/**").hasRole("CLIENT")
                
                // Endpoints employés (peuvent consulter les chambres et feedbacks)
                .requestMatchers(HttpMethod.GET, "/api/chambres/**").hasAnyRole("EMPLOYE", "GESTIONNAIRE")
                .requestMatchers(HttpMethod.GET, "/api/feedbacks/**").hasAnyRole("EMPLOYE", "GESTIONNAIRE")
                .requestMatchers(HttpMethod.PUT, "/api/chambres/*/disponibilite").hasAnyRole("EMPLOYE", "GESTIONNAIRE")
                
                // Endpoints gestionnaires (accès complet)
                .requestMatchers("/api/gestionnaires/**").hasRole("GESTIONNAIRE")
                .requestMatchers("/api/employes/**").hasRole("GESTIONNAIRE")
                .requestMatchers(HttpMethod.POST, "/api/chambres").hasRole("GESTIONNAIRE")
                .requestMatchers(HttpMethod.PUT, "/api/chambres/**").hasRole("GESTIONNAIRE")
                .requestMatchers(HttpMethod.DELETE, "/api/chambres/**").hasRole("GESTIONNAIRE")
                .requestMatchers(HttpMethod.GET, "/api/reservations").hasRole("GESTIONNAIRE")
                .requestMatchers(HttpMethod.PUT, "/api/reservations/**").hasRole("GESTIONNAIRE")
                .requestMatchers(HttpMethod.DELETE, "/api/reservations/**").hasRole("GESTIONNAIRE")
                .requestMatchers("/api/reservations/*/annuler").hasRole("GESTIONNAIRE")
                .requestMatchers("/api/reservations/*/confirmer").hasRole("GESTIONNAIRE")
                .requestMatchers("/api/reservations/*/terminer").hasRole("GESTIONNAIRE")
                
                // Tout le reste nécessite une authentification
                .anyRequest().authenticated()
            );

        // Ajout du filtre JWT
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        // Configuration pour H2 Console
        http.headers(headers -> headers.frameOptions().disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}