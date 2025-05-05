package itbs.mohamedlandolsi.gestioncommandeslivraisons.repository;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Paiement;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Paiement.StatutPaiement;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Paiement.ModePaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    List<Paiement> findByCommandeId(Long commandeId);
    List<Paiement> findByMode(ModePaiement mode);
    List<Paiement> findByStatut(StatutPaiement statut);
    List<Paiement> findByDateBetween(LocalDateTime start, LocalDateTime end);
    List<Paiement> findByDateAfter(LocalDateTime fromDate);
}