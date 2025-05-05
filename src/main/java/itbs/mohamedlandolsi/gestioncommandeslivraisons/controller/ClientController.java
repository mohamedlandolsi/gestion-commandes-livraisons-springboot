package itbs.mohamedlandolsi.gestioncommandeslivraisons.controller;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Client;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // Get all clients
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    // Get client by ID
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return clientService.getClientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new client
    @PostMapping
    public ResponseEntity<Client> createClient(@Valid @RequestBody Client client) {
        // Ensure we're creating a new client, not updating
        if (client.getId() != null && clientService.existsById(client.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Client savedClient = clientService.saveClient(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
    }

    // Update an existing client
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @Valid @RequestBody Client client) {
        if (!clientService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        client.setId(id); // Ensure ID consistency
        Client updatedClient = clientService.saveClient(client);
        return ResponseEntity.ok(updatedClient);
    }

    // Delete a client
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        if (!clientService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    // Search clients by name
    @GetMapping("/search")
    public ResponseEntity<List<Client>> searchClients(@RequestParam String query) {
        List<Client> clients = clientService.searchClientsByName(query);
        return ResponseEntity.ok(clients);
    }

    // Get clients by city
    @GetMapping("/by-city")
    public ResponseEntity<List<Client>> getClientsByCity(@RequestParam String city) {
        List<Client> clients = clientService.getClientsByCity(city);
        return ResponseEntity.ok(clients);
    }

    // Get clients by postal code
    @GetMapping("/by-postal-code")
    public ResponseEntity<List<Client>> getClientsByPostalCode(@RequestParam String postalCode) {
        List<Client> clients = clientService.getClientsByPostalCode(postalCode);
        return ResponseEntity.ok(clients);
    }

    // Get client by email
    @GetMapping("/by-email")
    public ResponseEntity<Client> getClientByEmail(@RequestParam String email) {
        return clientService.getClientByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}