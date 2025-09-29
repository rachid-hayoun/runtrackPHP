package com.example.Overlook_Hotel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Overlook_Hotel.model.Client;
import com.example.Overlook_Hotel.repository.ClientRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Integer id) {
        return clientRepository.findById(id);
    }

    public Client createClient(Client client) {
        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new RuntimeException("Le client avec cet email existe déjà");
        }
        
        client.setPointsFidelite(0);
        
        return clientRepository.save(client);
    }

    public Client updateClient(Integer id, Client clientDetails) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            client.setNom(clientDetails.getNom());
            client.setPrenom(clientDetails.getPrenom());
            client.setEmail(clientDetails.getEmail());
            client.setTelephone(clientDetails.getTelephone());
            
            return clientRepository.save(client);
        } else {
            throw new RuntimeException("Client non trouvé avec l'ID: " + id);
        }
    }

    public void deleteClient(Integer id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
        } else {
            throw new RuntimeException("Client non trouvé avec l'ID: " + id);
        }
    }

    public Client addPoints(Integer id, Integer points) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            client.setPointsFidelite(client.getPoints() + points);
            return clientRepository.save(client);
        } else {
            throw new RuntimeException("Client non trouvé");
        }
    }

    public Client usePoints(Integer id, Integer points) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            if (client.getPoints() >= points) {
                client.setPointsFidelite(client.getPoints() - points);
                return clientRepository.save(client);
            } else {
                throw new RuntimeException("Points insuffisants");
            }
        } else {
            throw new RuntimeException("Client non trouvé");
        }
    }

    public Optional<Client> findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public List<Client> findByNom(String nom) {
        return clientRepository.findByNom(nom);
    }

    public List<Client> findByPrenom(String prenom) {
        return clientRepository.findByPrenom(prenom);
    }

    public Optional<Client> findByTelephone(String telephone) {
        return clientRepository.findByTelephone(telephone);
    }

    public List<Client> findByPoints(Integer points) {
        return clientRepository.findByPoints(points);
    }

    public boolean existsByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }

    public Optional<Client> login(String email, String motDePasse) {
        Optional<Client> client = clientRepository.findByEmail(email);
        if (client.isPresent() && client.get().getMotDePasse().equals(motDePasse)) {
            return client;
        }
        return Optional.empty();
    }
}
