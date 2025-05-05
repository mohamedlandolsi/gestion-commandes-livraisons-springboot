package itbs.mohamedlandolsi.gestioncommandeslivraisons.service;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Commande;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Commande.StatutCommande;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommandeService {

    private final CommandeRepository commandeRepository;

    @Autowired
    public CommandeService(CommandeRepository commandeRepository) {
        this.commandeRepository = commandeRepository;
    }

    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    public Optional<Commande> getCommandeById(Long id) {
        return commandeRepository.findById(id);
    }

    public Commande saveCommande(Commande commande) {
        return commandeRepository.save(commande);
    }

    public void deleteCommande(Long id) {
        commandeRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return commandeRepository.existsById(id);
    }

    public Commande updateCommandeStatus(Long id, StatutCommande statut) {
        Optional<Commande> commandeOpt = commandeRepository.findById(id);
        if (commandeOpt.isEmpty()) {
            return null;
        }
        
        Commande commande = commandeOpt.get();
        commande.setStatut(statut);
        return commandeRepository.save(commande);
    }

    public List<Commande> getCommandesByClientId(Long clientId) {
        return commandeRepository.findByClientId(clientId);
    }

    public List<Commande> getCommandesByStatus(StatutCommande status) {
        return commandeRepository.findByStatut(status);
    }

    public List<Commande> getCommandesByDateRange(LocalDateTime start, LocalDateTime end) {
        return commandeRepository.findByDateBetween(start, end);
    }

    public List<Commande> getRecentCommandes(LocalDateTime fromDate) {
        return commandeRepository.findByDateAfterOrderByDateDesc(fromDate);
    }
}