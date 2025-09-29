package com.example.Overlook_Hotel.security;

import com.example.Overlook_Hotel.model.Client;
import com.example.Overlook_Hotel.model.Gestionnaire;
import com.example.Overlook_Hotel.repository.ClientRepository;
import com.example.Overlook_Hotel.repository.GestionnaireRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;
    private final GestionnaireRepository gestionnaireRepository;

    public CustomUserDetailsService(ClientRepository clientRepository, 
                                  GestionnaireRepository gestionnaireRepository) {
        this.clientRepository = clientRepository;
        this.gestionnaireRepository = gestionnaireRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Chercher d'abord dans les clients
        Optional<Client> client = clientRepository.findByEmail(email);
        if (client.isPresent()) {
            return createUserDetails(client.get().getEmail(), client.get().getMotDePasse(), 
                                   Role.CLIENT, client.get().getIdClient());
        }

        // Chercher dans les gestionnaires
        Optional<Gestionnaire> gestionnaire = gestionnaireRepository.findByEmail(email);
        if (gestionnaire.isPresent()) {
            return createUserDetails(gestionnaire.get().getEmail(), gestionnaire.get().getMotDePasse(), 
                                   Role.GESTIONNAIRE, gestionnaire.get().getIdGestionnaire());
        }

        throw new UsernameNotFoundException("Utilisateur non trouvé: " + email);
    }

    private UserDetails createUserDetails(String email, String password, Role role, Integer userId) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(email)
                .password(password)
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name())))
                .build();
    }
}