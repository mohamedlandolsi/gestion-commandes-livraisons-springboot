package itbs.mohamedlandolsi.gestioncommandeslivraisons.controller;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.dto.LivraisonRequestDTO;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Livraison;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Livraison.StatutLivraison;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.service.LivraisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/livraisons")
public class LivraisonController {

    private final LivraisonService livraisonService;

    @Autowired
    public LivraisonController(LivraisonService livraisonService) {
        this.livraisonService = livraisonService;
    }

    // Get all deliveries
    @GetMapping
    public ResponseEntity<List<Livraison>> getAllLivraisons() {
        List<Livraison> livraisons = livraisonService.getAllLivraisons();
        return ResponseEntity.ok(livraisons);
    }

    // Get delivery by ID
    @GetMapping("/{id}")
    public ResponseEntity<Livraison> getLivraisonById(@PathVariable Long id) {
        return livraisonService.getLivraisonById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new delivery
    @PostMapping
    public ResponseEntity<?> createLivraison(@Valid @RequestBody LivraisonRequestDTO livraisonDTO) {
        try {
            Livraison savedLivraison = livraisonService.createLivraisonFromDTO(livraisonDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedLivraison);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An unexpected error occurred on the server.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Update an existing delivery
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLivraison(
            @PathVariable Long id,
            @Valid @RequestBody LivraisonRequestDTO livraisonDTO) {
        
        try {
            Livraison updatedLivraison = livraisonService.updateLivraisonFromDTO(id, livraisonDTO);
            if (updatedLivraison == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedLivraison);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An unexpected error occurred on the server.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Delete a delivery
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivraison(@PathVariable Long id) {
        if (!livraisonService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        livraisonService.deleteLivraison(id);
        return ResponseEntity.noContent().build();
    }

    // Update delivery status with "statut" parameter
    @PatchMapping("/{id}/statut")
    public ResponseEntity<Livraison> updateLivraisonStatut(
            @PathVariable Long id,
            @RequestParam StatutLivraison statut) {
        
        Livraison updatedLivraison = livraisonService.updateLivraisonStatus(id, statut);
        if (updatedLivraison == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedLivraison);
    }

    // Update delivery status with "status" in URL and JSON body
    @PatchMapping("/{id}/status")
    public ResponseEntity<Livraison> updateLivraisonStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        
        String statusValue = statusUpdate.get("status");
        if (statusValue == null) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            StatutLivraison statut = StatutLivraison.valueOf(statusValue.toUpperCase());
            Livraison updatedLivraison = livraisonService.updateLivraisonStatus(id, statut);
            if (updatedLivraison == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedLivraison);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Assign transporter to delivery
    @PatchMapping("/{id}/transporteur")
    public ResponseEntity<Livraison> assignTransporteur(
            @PathVariable Long id,
            @RequestParam Long transporteurId) {
        Livraison updatedLivraison = livraisonService.assignTransporteur(id, transporteurId);
        if (updatedLivraison == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedLivraison);
    }

    // Get deliveries by order ID
    @GetMapping("/by-commande/{commandeId}")
    public ResponseEntity<List<Livraison>> getLivraisonsByCommande(@PathVariable Long commandeId) {
        List<Livraison> livraisons = livraisonService.getLivraisonsByCommandeId(commandeId);
        return ResponseEntity.ok(livraisons);
    }

    // Get deliveries by transporter ID
    @GetMapping("/by-transporteur/{transporteurId}")
    public ResponseEntity<List<Livraison>> getLivraisonsByTransporteur(@PathVariable Long transporteurId) {
        List<Livraison> livraisons = livraisonService.getLivraisonsByTransporteurId(transporteurId);
        return ResponseEntity.ok(livraisons);
    }

    // Get deliveries by status
    @GetMapping("/by-statut")
    public ResponseEntity<List<Livraison>> getLivraisonsByStatut(@RequestParam StatutLivraison statut) {
        List<Livraison> livraisons = livraisonService.getLivraisonsByStatut(statut);
        return ResponseEntity.ok(livraisons);
    }

    // Get deliveries between dates
    @GetMapping("/by-date-range")
    public ResponseEntity<List<Livraison>> getLivraisonsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        List<Livraison> livraisons = livraisonService.getLivraisonsByDateRange(start, end);
        return ResponseEntity.ok(livraisons);
    }

    // Get upcoming deliveries
    @GetMapping("/upcoming")
    public ResponseEntity<List<Livraison>> getUpcomingLivraisons(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from) {
        
        LocalDateTime fromDate = from != null ? from : LocalDateTime.now();
        List<Livraison> livraisons = livraisonService.getUpcomingLivraisons(fromDate);
        return ResponseEntity.ok(livraisons);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("message", "Validation failed");
        List<Map<String, String>> errorDetails = ex.getBindingResult().getAllErrors().stream()
            .map(error -> {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put("field", ((FieldError) error).getField());
                errorMap.put("message", error.getDefaultMessage());
                return errorMap;
            })
            .collect(Collectors.toList());
        errors.put("errors", errorDetails);
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Map<String, String> handleIllegalArgumentExceptions(IllegalArgumentException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return errorResponse;
    }
}