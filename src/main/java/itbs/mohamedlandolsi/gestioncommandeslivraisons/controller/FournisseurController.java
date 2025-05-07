package itbs.mohamedlandolsi.gestioncommandeslivraisons.controller;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Commande;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Fournisseur;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.service.FournisseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/fournisseurs")
public class FournisseurController {

    private final FournisseurService fournisseurService;

    @Autowired
    public FournisseurController(FournisseurService fournisseurService) {
        this.fournisseurService = fournisseurService;
    }

    @GetMapping
    public List<Fournisseur> getAllFournisseurs() {
        return fournisseurService.getAllFournisseurs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fournisseur> getFournisseurById(@PathVariable Long id) {
        return fournisseurService.getFournisseurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Fournisseur createFournisseur(@RequestBody Fournisseur fournisseur) {
        return fournisseurService.saveFournisseur(fournisseur);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fournisseur> updateFournisseur(@PathVariable Long id, @RequestBody Fournisseur fournisseur) {
        if (!fournisseurService.getFournisseurById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        fournisseur.setId(id);
        return ResponseEntity.ok(fournisseurService.saveFournisseur(fournisseur));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFournisseur(@PathVariable Long id) {
        if (!fournisseurService.getFournisseurById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        fournisseurService.deleteFournisseur(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<Fournisseur> searchFournisseurs(@RequestParam String nom) {
        return fournisseurService.searchFournisseursByNom(nom);
    }

    /**
     * Get order history for a supplier
     */
    @GetMapping("/{id}/commandes")
    public List<Commande> getOrderHistory(@PathVariable Long id) {
        return fournisseurService.getOrderHistoryByFournisseur(id);
    }

    /**
     * Get order history for a supplier within a date range
     */
    @GetMapping("/{id}/commandes/period")
    public List<Commande> getOrderHistoryByDateRange(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return fournisseurService.getOrderHistoryByDateRange(id, debut, fin);
    }

    /**
     * Update supplier rating
     */
    @PatchMapping("/{id}/note")
    public ResponseEntity<Fournisseur> updateRating(
            @PathVariable Long id, 
            @RequestParam Double note) {
        try {
            Fournisseur updated = fournisseurService.updateFournisseurRating(id, note);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}