package itbs.mohamedlandolsi.gestioncommandeslivraisons.service;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Commande;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.LigneCommande;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Livraison;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Livraison.StatutLivraison;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Produit;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Transporteur;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.repository.CommandeRepository;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.repository.LigneCommandeRepository;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.repository.LivraisonRepository;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.repository.TransporteurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LivraisonService {

    private final LivraisonRepository livraisonRepository;
    private final TransporteurRepository transporteurRepository;
    private final ProduitService produitService;
    private final CommandeRepository commandeRepository;
    private final LigneCommandeRepository ligneCommandeRepository;

    @Autowired
    public LivraisonService(
            LivraisonRepository livraisonRepository,
            TransporteurRepository transporteurRepository,
            ProduitService produitService,
            CommandeRepository commandeRepository,
            LigneCommandeRepository ligneCommandeRepository) {
        this.livraisonRepository = livraisonRepository;
        this.transporteurRepository = transporteurRepository;
        this.produitService = produitService;
        this.commandeRepository = commandeRepository;
        this.ligneCommandeRepository = ligneCommandeRepository;
    }

    public List<Livraison> getAllLivraisons() {
        return livraisonRepository.findAll();
    }

    public Optional<Livraison> getLivraisonById(Long id) {
        return livraisonRepository.findById(id);
    }

    public Livraison saveLivraison(Livraison livraison) {
        return livraisonRepository.save(livraison);
    }

    public void deleteLivraison(Long id) {
        livraisonRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return livraisonRepository.existsById(id);
    }

    /**
     * Updates the status of a delivery and handles stock updates if delivery is completed
     * @param id delivery ID
     * @param statut new status
     * @return updated delivery
     */
    @Transactional
    public Livraison updateLivraisonStatus(Long id, StatutLivraison statut) {
        Optional<Livraison> livraisonOpt = livraisonRepository.findById(id);
        if (livraisonOpt.isEmpty()) {
            return null;
        }
        
        Livraison livraison = livraisonOpt.get();
        StatutLivraison oldStatus = livraison.getStatut();
        livraison.setStatut(statut);
        
        // If the delivery status changes to LIVREE, update the stock
        if (statut == StatutLivraison.LIVREE && oldStatus != StatutLivraison.LIVREE) {
            updateStockOnDelivery(livraison);
        }
        
        return livraisonRepository.save(livraison);
    }

    /**
     * Updates stock quantities when a delivery is completed
     * @param livraison the completed delivery
     */
    private void updateStockOnDelivery(Livraison livraison) {
        Commande commande = livraison.getCommande();
        if (commande == null) {
            throw new IllegalStateException("No command associated with this delivery");
        }
        
        List<LigneCommande> lignesCommande = ligneCommandeRepository.findByCommandeId(commande.getId());
        
        for (LigneCommande ligne : lignesCommande) {
            Produit produit = ligne.getProduit();
            if (produit != null) {
                // Update stock - should be called only after confirming delivery
                produitService.reduceStock(produit.getId(), ligne.getQuantite());
            }
        }
        
        // Update order status to reflect delivery completion
        commande.setStatut(Commande.StatutCommande.LIVREE);
        commandeRepository.save(commande);
    }

    public Livraison assignTransporteur(Long livraisonId, Long transporteurId) {
        Optional<Livraison> livraisonOpt = livraisonRepository.findById(livraisonId);
        Optional<Transporteur> transporteurOpt = transporteurRepository.findById(transporteurId);
        
        if (livraisonOpt.isEmpty() || transporteurOpt.isEmpty()) {
            return null;
        }
        
        Livraison livraison = livraisonOpt.get();
        livraison.setTransporteur(transporteurOpt.get());
        return livraisonRepository.save(livraison);
    }

    public List<Livraison> getLivraisonsByCommandeId(Long commandeId) {
        return livraisonRepository.findByCommandeId(commandeId);
    }

    public List<Livraison> getLivraisonsByTransporteurId(Long transporteurId) {
        return livraisonRepository.findByTransporteurId(transporteurId);
    }

    public List<Livraison> getLivraisonsByStatut(StatutLivraison statut) {
        return livraisonRepository.findByStatut(statut);
    }

    public List<Livraison> getLivraisonsByDateRange(LocalDateTime start, LocalDateTime end) {
        return livraisonRepository.findByDateLivraisonBetween(start, end);
    }

    public List<Livraison> getUpcomingLivraisons(LocalDateTime fromDate) {
        return livraisonRepository.findByDateLivraisonAfterOrderByDateLivraison(fromDate);
    }
}
