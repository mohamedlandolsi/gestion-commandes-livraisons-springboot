package itbs.mohamedlandolsi.gestioncommandeslivraisons.service;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.LigneCommande;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.repository.LigneCommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LigneCommandeService {

    private final LigneCommandeRepository ligneCommandeRepository;

    @Autowired
    public LigneCommandeService(LigneCommandeRepository ligneCommandeRepository) {
        this.ligneCommandeRepository = ligneCommandeRepository;
    }

    public List<LigneCommande> getAllLignesCommande() {
        return ligneCommandeRepository.findAll();
    }

    public Optional<LigneCommande> getLigneCommandeById(Long id) {
        return ligneCommandeRepository.findById(id);
    }

    public LigneCommande saveLigneCommande(LigneCommande ligneCommande) {
        return ligneCommandeRepository.save(ligneCommande);
    }

    public void deleteLigneCommande(Long id) {
        ligneCommandeRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return ligneCommandeRepository.existsById(id);
    }

    public List<LigneCommande> getLignesCommandeByCommandeId(Long commandeId) {
        return ligneCommandeRepository.findByCommandeId(commandeId);
    }

    public List<LigneCommande> getLignesCommandeByProduitId(Long produitId) {
        return ligneCommandeRepository.findByProduitId(produitId);
    }

    public void deleteAllByCommandeId(Long commandeId) {
        ligneCommandeRepository.deleteByCommandeId(commandeId);
    }
}