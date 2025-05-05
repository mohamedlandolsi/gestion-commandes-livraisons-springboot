package itbs.mohamedlandolsi.gestioncommandeslivraisons.service;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Paiement;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Paiement.StatutPaiement;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Paiement.ModePaiement;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.repository.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaiementService {

    private final PaiementRepository paiementRepository;

    @Autowired
    public PaiementService(PaiementRepository paiementRepository) {
        this.paiementRepository = paiementRepository;
    }

    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }

    public Optional<Paiement> getPaiementById(Long id) {
        return paiementRepository.findById(id);
    }

    public Paiement savePaiement(Paiement paiement) {
        return paiementRepository.save(paiement);
    }

    public void deletePaiement(Long id) {
        paiementRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return paiementRepository.existsById(id);
    }

    public Paiement updatePaiementStatus(Long id, StatutPaiement statut) {
        Optional<Paiement> paiementOpt = paiementRepository.findById(id);
        if (paiementOpt.isEmpty()) {
            return null;
        }
        
        Paiement paiement = paiementOpt.get();
        paiement.setStatut(statut);
        return paiementRepository.save(paiement);
    }

    public Paiement processPaiement(Long id) {
        Optional<Paiement> paiementOpt = paiementRepository.findById(id);
        if (paiementOpt.isEmpty()) {
            return null;
        }
        
        Paiement paiement = paiementOpt.get();
        paiement.setStatut(StatutPaiement.EFFECTUE);
        return paiementRepository.save(paiement);
    }

    public List<Paiement> getPaiementsByCommandeId(Long commandeId) {
        return paiementRepository.findByCommandeId(commandeId);
    }

    public List<Paiement> getPaiementsByMode(ModePaiement mode) {
        return paiementRepository.findByMode(mode);
    }

    public List<Paiement> getPaiementsByStatut(StatutPaiement statut) {
        return paiementRepository.findByStatut(statut);
    }

    public List<Paiement> getPaiementsByDateRange(LocalDateTime start, LocalDateTime end) {
        return paiementRepository.findByDateBetween(start, end);
    }

    public List<Paiement> getRecentPaiements(LocalDateTime fromDate) {
        return paiementRepository.findByDateAfter(fromDate);
    }
}