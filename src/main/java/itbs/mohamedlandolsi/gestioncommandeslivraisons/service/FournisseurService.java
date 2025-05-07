package itbs.mohamedlandolsi.gestioncommandeslivraisons.service;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Commande;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Fournisseur;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.LigneCommande;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Produit;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.repository.FournisseurRepository;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.repository.LigneCommandeRepository;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FournisseurService {

    private final FournisseurRepository fournisseurRepository;
    private final ProduitRepository produitRepository;
    private final LigneCommandeRepository ligneCommandeRepository;

    @Autowired
    public FournisseurService(
            FournisseurRepository fournisseurRepository,
            ProduitRepository produitRepository,
            LigneCommandeRepository ligneCommandeRepository) {
        this.fournisseurRepository = fournisseurRepository;
        this.produitRepository = produitRepository;
        this.ligneCommandeRepository = ligneCommandeRepository;
    }

    /**
     * Get all suppliers
     * @return list of all suppliers
     */
    public List<Fournisseur> getAllFournisseurs() {
        return fournisseurRepository.findAll();
    }

    /**
     * Get a supplier by ID
     * @param id supplier ID
     * @return supplier if found
     */
    public Optional<Fournisseur> getFournisseurById(Long id) {
        return fournisseurRepository.findById(id);
    }

    /**
     * Save or update a supplier
     * @param fournisseur supplier to save
     * @return saved supplier
     */
    public Fournisseur saveFournisseur(Fournisseur fournisseur) {
        return fournisseurRepository.save(fournisseur);
    }

    /**
     * Delete a supplier by ID
     * @param id supplier ID
     */
    public void deleteFournisseur(Long id) {
        fournisseurRepository.deleteById(id);
    }

    /**
     * Search suppliers by name
     * @param nom name to search
     * @return matching suppliers
     */
    public List<Fournisseur> searchFournisseursByNom(String nom) {
        return fournisseurRepository.findByNomContainingIgnoreCase(nom);
    }

    /**
     * Gets all orders for a specific supplier by tracking products from this supplier
     * @param fournisseurId supplier ID
     * @return list of orders containing products from this supplier
     */
    public List<Commande> getOrderHistoryByFournisseur(Long fournisseurId) {
        // First get all products from this supplier
        List<Produit> produitsDuFournisseur = produitRepository.findByFournisseurId(fournisseurId);
        
        if (produitsDuFournisseur.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Get IDs of these products
        List<Long> productIds = produitsDuFournisseur.stream()
                .map(Produit::getId)
                .collect(Collectors.toList());
        
        // Get all order lines containing these products
        List<LigneCommande> lignesCommande = ligneCommandeRepository.findByProduitIdIn(productIds);
        
        // Extract unique orders
        Set<Commande> commandes = new HashSet<>();
        for (LigneCommande ligne : lignesCommande) {
            if (ligne.getCommande() != null) {
                commandes.add(ligne.getCommande());
            }
        }
        
        // Convert to list and sort by date descending
        return commandes.stream()
                .sorted(Comparator.comparing(Commande::getDate).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Gets order history for a supplier within a date range
     * @param fournisseurId supplier ID
     * @param debut start date
     * @param fin end date
     * @return filtered list of orders
     */
    public List<Commande> getOrderHistoryByDateRange(Long fournisseurId, LocalDateTime debut, LocalDateTime fin) {
        List<Commande> allOrders = getOrderHistoryByFournisseur(fournisseurId);
        
        return allOrders.stream()
                .filter(commande -> {
                    LocalDateTime orderDate = commande.getDate();
                    return orderDate != null && 
                           (orderDate.isEqual(debut) || orderDate.isAfter(debut)) &&
                           (orderDate.isEqual(fin) || orderDate.isBefore(fin));
                })
                .collect(Collectors.toList());
    }

    /**
     * Updates the rating for a supplier
     * @param fournisseurId supplier ID
     * @param note new rating (0-5)
     * @return updated supplier
     */
    public Fournisseur updateFournisseurRating(Long fournisseurId, Double note) {
        if (note < 0 || note > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }
        
        Optional<Fournisseur> fournisseurOpt = fournisseurRepository.findById(fournisseurId);
        if (fournisseurOpt.isEmpty()) {
            throw new IllegalArgumentException("Supplier not found: " + fournisseurId);
        }
        
        Fournisseur fournisseur = fournisseurOpt.get();
        fournisseur.setNote(note);
        return fournisseurRepository.save(fournisseur);
    }
}