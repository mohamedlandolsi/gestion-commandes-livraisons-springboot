package itbs.mohamedlandolsi.gestioncommandeslivraisons.repository;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Transporteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransporteurRepository extends JpaRepository<Transporteur, Long> {
    List<Transporteur> findByNomContainingIgnoreCase(String nom);
}