package itbs.mohamedlandolsi.gestioncommandeslivraisons.repository;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Commande;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Commande.StatutCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByClientId(Long clientId);
    List<Commande> findByStatut(StatutCommande status);
    List<Commande> findByDateBetween(LocalDateTime start, LocalDateTime end);
    List<Commande> findByDateAfterOrderByDateDesc(LocalDateTime fromDate);
}