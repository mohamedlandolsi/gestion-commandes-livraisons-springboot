package itbs.mohamedlandolsi.gestioncommandeslivraisons.service;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Livraison;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Livraison.StatutLivraison;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Transporteur;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.repository.LivraisonRepository;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.repository.TransporteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LivraisonService {

    private final LivraisonRepository livraisonRepository;
    private final TransporteurRepository transporteurRepository;

    @Autowired
    public LivraisonService(LivraisonRepository livraisonRepository, TransporteurRepository transporteurRepository) {
        this.livraisonRepository = livraisonRepository;
        this.transporteurRepository = transporteurRepository;
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

    public Livraison updateLivraisonStatus(Long id, StatutLivraison statut) {
        Optional<Livraison> livraisonOpt = livraisonRepository.findById(id);
        if (livraisonOpt.isEmpty()) {
            return null;
        }
        
        Livraison livraison = livraisonOpt.get();
        livraison.setStatut(statut);
        return livraisonRepository.save(livraison);
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
