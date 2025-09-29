package com.example.Overlook_Hotel.controller;

import com.example.Overlook_Hotel.dto.AuthResponse;
import com.example.Overlook_Hotel.dto.ClientRegistrationRequest;
import com.example.Overlook_Hotel.dto.LoginRequest;
import com.example.Overlook_Hotel.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse authResponse = authService.authenticate(loginRequest);
            return ResponseEntity.ok(authResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Erreur d'authentification", e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerClient(@Valid @RequestBody ClientRegistrationRequest request) {
        try {
            AuthResponse authResponse = authService.registerClient(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("Erreur d'inscription", e.getMessage()));
        }
    }

    // Classe interne pour les réponses d'erreur
    public static class ErrorResponse {
        private String error;
        private String message;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }

        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}