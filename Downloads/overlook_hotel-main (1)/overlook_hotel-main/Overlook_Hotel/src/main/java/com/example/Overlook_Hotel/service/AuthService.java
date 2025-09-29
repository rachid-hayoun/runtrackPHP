package com.example.Overlook_Hotel.service;

import com.example.Overlook_Hotel.dto.AuthResponse;
import com.example.Overlook_Hotel.dto.ClientRegistrationRequest;
import com.example.Overlook_Hotel.dto.LoginRequest;
import com.example.Overlook_Hotel.model.Client;
import com.example.Overlook_Hotel.model.Gestionnaire;
import com.example.Overlook_Hotel.repository.ClientRepository;
import com.example.Overlook_Hotel.repository.GestionnaireRepository;
import com.example.Overlook_Hotel.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final GestionnaireRepository gestionnaireRepository;

    public AuthService(AuthenticationManager authenticationManager,
                      JwtUtils jwtUtils,
                      PasswordEncoder passwordEncoder,
                      ClientRepository clientRepository,
                      GestionnaireRepository gestionnaireRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.clientRepository = clientRepository;
        this.gestionnaireRepository = gestionnaireRepository;
    }

    public AuthResponse authenticate(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getMotDePasse()
                )
            );

            String token = jwtUtils.generateToken(authentication);
            
            // Récupérer les informations de l'utilisateur
            String email = loginRequest.getEmail();
            
            // Chercher d'abord dans les clients
            Optional<Client> client = clientRepository.findByEmail(email);
            if (client.isPresent()) {
                Client c = client.get();
                return new AuthResponse(token, email, "CLIENT", c.getIdClient(), c.getNom(), c.getPrenom());
            }

            // Chercher dans les gestionnaires
            Optional<Gestionnaire> gestionnaire = gestionnaireRepository.findByEmail(email);
            if (gestionnaire.isPresent()) {
                Gestionnaire g = gestionnaire.get();
                return new AuthResponse(token, email, "GESTIONNAIRE", g.getIdGestionnaire(), g.getNom(), g.getPrenom());
            }

            throw new RuntimeException("Utilisateur non trouvé");

        } catch (AuthenticationException e) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }
    }

    public AuthResponse registerClient(ClientRegistrationRequest request) {
        // Vérifier si l'email existe déjà
        if (clientRepository.findByEmail(request.getEmail()).isPresent() ||
            gestionnaireRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Un compte avec cet email existe déjà");
        }

        // Créer le nouveau client
        Client client = new Client();
        client.setNom(request.getNom());
        client.setPrenom(request.getPrenom());
        client.setEmail(request.getEmail());
        client.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        client.setTelephone(request.getTelephone());
        client.setPointsFidelite(0);

        Client savedClient = clientRepository.save(client);

        // Authentifier automatiquement le nouveau client
        LoginRequest loginRequest = new LoginRequest(request.getEmail(), request.getMotDePasse());
        return authenticate(loginRequest);
    }
}