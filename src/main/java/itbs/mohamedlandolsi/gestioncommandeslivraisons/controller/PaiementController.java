package itbs.mohamedlandolsi.gestioncommandeslivraisons.controller;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Paiement;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Paiement.StatutPaiement;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Paiement.ModePaiement;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.service.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/paiements")
public class PaiementController {

    private final PaiementService paiementService;

    @Autowired
    public PaiementController(PaiementService paiementService) {
        this.paiementService = paiementService;
    }

    // Get all payments
    @GetMapping
    public ResponseEntity<List<Paiement>> getAllPaiements() {
        List<Paiement> paiements = paiementService.getAllPaiements();
        return ResponseEntity.ok(paiements);
    }

    // Get payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Paiement> getPaiementById(@PathVariable Long id) {
        return paiementService.getPaiementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new payment
    @PostMapping
    public ResponseEntity<Paiement> createPaiement(@Valid @RequestBody Paiement paiement) {
        if (paiement.getId() != null && paiementService.existsById(paiement.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Paiement savedPaiement = paiementService.savePaiement(paiement);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPaiement);
    }

    // Update an existing payment
    @PutMapping("/{id}")
    public ResponseEntity<Paiement> updatePaiement(
            @PathVariable Long id,
            @Valid @RequestBody Paiement paiementDetails) {
        
        Optional<Paiement> existingPaiementOptional = paiementService.getPaiementById(id);

        if (existingPaiementOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Paiement existingPaiement = existingPaiementOptional.get();

        if (paiementDetails.getDate() != null) {
            existingPaiement.setDate(paiementDetails.getDate());
        }
        if (paiementDetails.getMontantPaye() != null) {
            existingPaiement.setMontantPaye(paiementDetails.getMontantPaye());
        }
        if (paiementDetails.getMode() != null) {
            existingPaiement.setMode(paiementDetails.getMode());
        }
        if (paiementDetails.getStatut() != null) {
            existingPaiement.setStatut(paiementDetails.getStatut());
        }

        Paiement updatedPaiement = paiementService.savePaiement(existingPaiement);
        return ResponseEntity.ok(updatedPaiement);
    }

    // Delete a payment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaiement(@PathVariable Long id) {
        if (!paiementService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        paiementService.deletePaiement(id);
        return ResponseEntity.noContent().build();
    }

    // Update payment status
    @PatchMapping("/{id}/statut")
    public ResponseEntity<Paiement> updatePaiementStatut(
            @PathVariable Long id,
            @RequestParam StatutPaiement statut) {
        
        Paiement updatedPaiement = paiementService.updatePaiementStatus(id, statut);
        if (updatedPaiement == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPaiement);
    }

    // Process a payment
    @PostMapping("/{id}/process")
    public ResponseEntity<Paiement> processPaiement(@PathVariable Long id) {
        Paiement processedPaiement = paiementService.processPaiement(id);
        if (processedPaiement == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(processedPaiement);
    }

    // Get payments by order ID
    @GetMapping("/by-commande/{commandeId}")
    public ResponseEntity<List<Paiement>> getPaiementsByCommande(@PathVariable Long commandeId) {
        List<Paiement> paiements = paiementService.getPaiementsByCommandeId(commandeId);
        return ResponseEntity.ok(paiements);
    }

    // Get payments by payment method
    @GetMapping("/by-mode")
    public ResponseEntity<List<Paiement>> getPaiementsByMode(@RequestParam ModePaiement mode) {
        List<Paiement> paiements = paiementService.getPaiementsByMode(mode);
        return ResponseEntity.ok(paiements);
    }

    // Get payments by status
    @GetMapping("/by-statut")
    public ResponseEntity<List<Paiement>> getPaiementsByStatut(@RequestParam StatutPaiement statut) {
        List<Paiement> paiements = paiementService.getPaiementsByStatut(statut);
        return ResponseEntity.ok(paiements);
    }

    // Get payments between dates
    @GetMapping("/by-date-range")
    public ResponseEntity<List<Paiement>> getPaiementsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        List<Paiement> paiements = paiementService.getPaiementsByDateRange(start, end);
        return ResponseEntity.ok(paiements);
    }

    // Get recent payments
    @GetMapping("/recent")
    public ResponseEntity<List<Paiement>> getRecentPaiements(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        
        LocalDateTime fromDate = since != null ? since : LocalDateTime.now().minusDays(30);
        List<Paiement> paiements = paiementService.getRecentPaiements(fromDate);
        return ResponseEntity.ok(paiements);
    }
}