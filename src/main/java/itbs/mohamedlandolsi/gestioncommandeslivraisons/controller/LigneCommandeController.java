package itbs.mohamedlandolsi.gestioncommandeslivraisons.controller;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.LigneCommande;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.service.LigneCommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/lignes-commandes")
public class LigneCommandeController {

    private final LigneCommandeService ligneCommandeService;

    @Autowired
    public LigneCommandeController(LigneCommandeService ligneCommandeService) {
        this.ligneCommandeService = ligneCommandeService;
    }

    // Get all order line items
    @GetMapping
    public ResponseEntity<List<LigneCommande>> getAllLignesCommandes() {
        List<LigneCommande> lignesCommandes = ligneCommandeService.getAllLignesCommande();
        return ResponseEntity.ok(lignesCommandes);
    }

    // Get order line item by ID
    @GetMapping("/{id}")
    public ResponseEntity<LigneCommande> getLigneCommandeById(@PathVariable Long id) {
        return ligneCommandeService.getLigneCommandeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new order line item
    @PostMapping
    public ResponseEntity<LigneCommande> createLigneCommande(@Valid @RequestBody LigneCommande ligneCommande) {
        if (ligneCommande.getId() != null && ligneCommandeService.existsById(ligneCommande.getId())) {
            return ResponseEntity.badRequest().build();
        }
        LigneCommande savedLigneCommande = ligneCommandeService.saveLigneCommande(ligneCommande);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLigneCommande);
    }

    // Update an existing order line item
    @PutMapping("/{id}")
    public ResponseEntity<LigneCommande> updateLigneCommande(
            @PathVariable Long id, 
            @Valid @RequestBody LigneCommande ligneCommande) {
        
        if (!ligneCommandeService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ligneCommande.setId(id);
        LigneCommande updatedLigneCommande = ligneCommandeService.saveLigneCommande(ligneCommande);
        return ResponseEntity.ok(updatedLigneCommande);
    }

    // Delete an order line item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLigneCommande(@PathVariable Long id) {
        if (!ligneCommandeService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ligneCommandeService.deleteLigneCommande(id);
        return ResponseEntity.noContent().build();
    }

    // Get line items by order ID
    @GetMapping("/by-commande/{commandeId}")
    public ResponseEntity<List<LigneCommande>> getLignesCommandeByCommande(@PathVariable Long commandeId) {
        List<LigneCommande> lignesCommandes = ligneCommandeService.getLignesCommandeByCommandeId(commandeId);
        return ResponseEntity.ok(lignesCommandes);
    }

    // Get line items by product ID
    @GetMapping("/by-produit/{produitId}")
    public ResponseEntity<List<LigneCommande>> getLignesCommandeByProduit(@PathVariable Long produitId) {
        List<LigneCommande> lignesCommandes = ligneCommandeService.getLignesCommandeByProduitId(produitId);
        return ResponseEntity.ok(lignesCommandes);
    }

    // Delete all line items for an order
    @DeleteMapping("/by-commande/{commandeId}")
    public ResponseEntity<Void> deleteAllByCommandeId(@PathVariable Long commandeId) {
        ligneCommandeService.deleteAllByCommandeId(commandeId);
        return ResponseEntity.noContent().build();
    }
}