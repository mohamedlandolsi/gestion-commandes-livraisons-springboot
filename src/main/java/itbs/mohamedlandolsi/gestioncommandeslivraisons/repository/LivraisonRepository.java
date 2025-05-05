package itbs.mohamedlandolsi.gestioncommandeslivraisons.repository;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Livraison;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Livraison.StatutLivraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LivraisonRepository extends JpaRepository<Livraison, Long> {
    List<Livraison> findByCommandeId(Long commandeId);
    List<Livraison> findByTransporteurId(Long transporteurId);
    List<Livraison> findByStatut(StatutLivraison statut);
    List<Livraison> findByDateLivraisonBetween(LocalDateTime start, LocalDateTime end);
    List<Livraison> findByDateLivraisonAfterOrderByDateLivraison(LocalDateTime fromDate);
}