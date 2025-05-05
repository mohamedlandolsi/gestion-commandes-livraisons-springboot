package itbs.mohamedlandolsi.gestioncommandeslivraisons.controller;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Transporteur;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.service.TransporteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/transporteurs")
public class TransporteurController {

    private final TransporteurService transporteurService;

    @Autowired
    public TransporteurController(TransporteurService transporteurService) {
        this.transporteurService = transporteurService;
    }

    // Get all transporters
    @GetMapping
    public ResponseEntity<List<Transporteur>> getAllTransporteurs() {
        List<Transporteur> transporteurs = transporteurService.getAllTransporteurs();
        return ResponseEntity.ok(transporteurs);
    }

    // Get transporter by ID
    @GetMapping("/{id}")
    public ResponseEntity<Transporteur> getTransporteurById(@PathVariable Long id) {
        return transporteurService.getTransporteurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new transporter
    @PostMapping
    public ResponseEntity<Transporteur> createTransporteur(@Valid @RequestBody Transporteur transporteur) {
        if (transporteur.getId() != null && transporteurService.existsById(transporteur.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Transporteur savedTransporteur = transporteurService.saveTransporteur(transporteur);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransporteur);
    }

    // Update an existing transporter
    @PutMapping("/{id}")
    public ResponseEntity<Transporteur> updateTransporteur(
            @PathVariable Long id,
            @Valid @RequestBody Transporteur transporteur) {
        
        if (!transporteurService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        transporteur.setId(id);
        Transporteur updatedTransporteur = transporteurService.saveTransporteur(transporteur);
        return ResponseEntity.ok(updatedTransporteur);
    }

    // Delete a transporter
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransporteur(@PathVariable Long id) {
        if (!transporteurService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        transporteurService.deleteTransporteur(id);
        return ResponseEntity.noContent().build();
    }

    // Get transporters by name
    @GetMapping("/by-nom")
    public ResponseEntity<List<Transporteur>> getTransporteursByNom(@RequestParam String nom) {
        List<Transporteur> transporteurs = transporteurService.getTransporteursByNom(nom);
        return ResponseEntity.ok(transporteurs);
    }

    // Search transporters
    @GetMapping("/search")
    public ResponseEntity<List<Transporteur>> searchTransporteurs(@RequestParam String query) {
        List<Transporteur> transporteurs = transporteurService.searchTransporteurs(query);
        return ResponseEntity.ok(transporteurs);
    }
}