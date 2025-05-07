package itbs.mohamedlandolsi.gestioncommandeslivraisons.service;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Produit;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.repository.ProduitRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;

    @Autowired
    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    public Optional<Produit> getProduitById(Long id) {
        return produitRepository.findById(id);
    }

    public Produit saveProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    public void deleteProduit(Long id) {
        produitRepository.deleteById(id);
    }

    public List<Produit> searchProduitsByNom(String nom) {
        return produitRepository.findByNomContainingIgnoreCase(nom);
    }

    /**
     * Checks if there's sufficient stock for a product
     * @param produitId product ID
     * @param quantite quantity needed
     * @return true if there is sufficient stock, false otherwise
     */
    public boolean hasEnoughStock(Long produitId, Integer quantite) {
        Optional<Produit> produitOpt = produitRepository.findById(produitId);
        if (produitOpt.isEmpty()) {
            return false;
        }
        
        Produit produit = produitOpt.get();
        return produit.getStock() >= quantite;
    }
    
    /**
     * Reduces stock quantity when products are delivered
     * @param produitId product ID
     * @param quantite quantity to reduce
     * @return updated Product
     * @throws IllegalStateException if not enough stock
     */
    @Transactional
    public Produit reduceStock(Long produitId, Integer quantite) {
        Optional<Produit> produitOpt = produitRepository.findById(produitId);
        if (produitOpt.isEmpty()) {
            throw new IllegalArgumentException("Product not found: " + produitId);
        }
        
        Produit produit = produitOpt.get();
        if (produit.getStock() < quantite) {
            throw new IllegalStateException("Insufficient stock for product: " + produit.getNom());
        }
        
        produit.setStock(produit.getStock() - quantite);
        return produitRepository.save(produit);
    }
    
    /**
     * Adds stock quantity when new stock is received
     * @param produitId product ID
     * @param quantite quantity to add
     * @return updated Product
     */
    @Transactional
    public Produit addStock(Long produitId, Integer quantite) {
        Optional<Produit> produitOpt = produitRepository.findById(produitId);
        if (produitOpt.isEmpty()) {
            throw new IllegalArgumentException("Product not found: " + produitId);
        }
        
        Produit produit = produitOpt.get();
        produit.setStock(produit.getStock() + quantite);
        return produitRepository.save(produit);
    }
}