package itbs.mohamedlandolsi.gestioncommandeslivraisons.controller;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Commande;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Commande.StatutCommande;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    private final CommandeService commandeService;

    @Autowired
    public CommandeController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }

    // Get all orders
    @GetMapping
    public ResponseEntity<List<Commande>> getAllCommandes() {
        List<Commande> commandes = commandeService.getAllCommandes();
        return ResponseEntity.ok(commandes);
    }

    // Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Commande> getCommandeById(@PathVariable Long id) {
        return commandeService.getCommandeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new order
    @PostMapping
    public ResponseEntity<?> createCommande(@Valid @RequestBody Commande commande) {
        if (commande.getId() != null && commandeService.existsById(commande.getId())) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            Commande savedCommande = commandeService.saveCommande(commande);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCommande);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // Update an existing order
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCommande(@PathVariable Long id, @Valid @RequestBody Commande commande) {
        if (!commandeService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        commande.setId(id);
        try {
            Commande updatedCommande = commandeService.saveCommande(commande);
            return ResponseEntity.ok(updatedCommande);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // Delete an order
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        if (!commandeService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        commandeService.deleteCommande(id);
        return ResponseEntity.noContent().build();
    }

    // Update order status
    @PatchMapping("/{id}/statut")
    public ResponseEntity<Commande> updateCommandeStatut(
            @PathVariable Long id,
            @RequestParam StatutCommande statut) {
        
        Commande updatedCommande = commandeService.updateCommandeStatus(id, statut);
        if (updatedCommande == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedCommande);
    }

    // Get orders by client ID
    @GetMapping("/by-client/{clientId}")
    public ResponseEntity<List<Commande>> getCommandesByClient(@PathVariable Long clientId) {
        List<Commande> commandes = commandeService.getCommandesByClientId(clientId);
        return ResponseEntity.ok(commandes);
    }

    // Get orders by status
    @GetMapping("/by-status")
    public ResponseEntity<List<Commande>> getCommandesByStatus(@RequestParam StatutCommande status) {
        List<Commande> commandes = commandeService.getCommandesByStatus(status);
        return ResponseEntity.ok(commandes);
    }

    // Get orders between dates
    @GetMapping("/by-date-range")
    public ResponseEntity<List<Commande>> getCommandesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        List<Commande> commandes = commandeService.getCommandesByDateRange(start, end);
        return ResponseEntity.ok(commandes);
    }

    // Get recent orders
    @GetMapping("/recent")
    public ResponseEntity<List<Commande>> getRecentCommandes(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        
        LocalDateTime fromDate = since != null ? since : LocalDateTime.now().minusDays(30);
        List<Commande> commandes = commandeService.getRecentCommandes(fromDate);
        return ResponseEntity.ok(commandes);
    }
}