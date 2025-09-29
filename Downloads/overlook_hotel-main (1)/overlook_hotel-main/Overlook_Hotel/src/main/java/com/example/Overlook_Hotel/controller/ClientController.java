package com.example.Overlook_Hotel.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Overlook_Hotel.model.Client;
import com.example.Overlook_Hotel.service.ClientService;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Integer id) {
        Optional<Client> client = clientService.getClientById(id);
        return client.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        try {
            Client savedClient = clientService.createClient(client);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Integer id, @RequestBody Client clientDetails) {
        try {
            Client updatedClient = clientService.updateClient(id, clientDetails);
            return ResponseEntity.ok(updatedClient);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Integer id) {
        try {
            clientService.deleteClient(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Client> getClientByEmail(@PathVariable String email) {
        Optional<Client> client = clientService.findByEmail(email);
        return client.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/add-points")
    public ResponseEntity<Client> addPoints(@PathVariable Integer id, @RequestParam Integer points) {
        try {
            Client client = clientService.addPoints(id, points);
            return ResponseEntity.ok(client);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}