package itbs.mohamedlandolsi.gestioncommandeslivraisons.repository;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.LigneCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LigneCommandeRepository extends JpaRepository<LigneCommande, Long> {
    List<LigneCommande> findByCommandeId(Long commandeId);
    List<LigneCommande> findByProduitId(Long produitId);
    void deleteByCommandeId(Long commandeId);
}