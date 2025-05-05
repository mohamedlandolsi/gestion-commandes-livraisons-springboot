package itbs.mohamedlandolsi.gestioncommandeslivraisons.service;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Transporteur;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.repository.TransporteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransporteurService {

    private final TransporteurRepository transporteurRepository;

    @Autowired
    public TransporteurService(TransporteurRepository transporteurRepository) {
        this.transporteurRepository = transporteurRepository;
    }

    public List<Transporteur> getAllTransporteurs() {
        return transporteurRepository.findAll();
    }

    public Optional<Transporteur> getTransporteurById(Long id) {
        return transporteurRepository.findById(id);
    }

    public Transporteur saveTransporteur(Transporteur transporteur) {
        return transporteurRepository.save(transporteur);
    }

    public void deleteTransporteur(Long id) {
        transporteurRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return transporteurRepository.existsById(id);
    }

    public List<Transporteur> getTransporteursByNom(String nom) {
        return transporteurRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<Transporteur> searchTransporteurs(String query) {
        return transporteurRepository.findByNomContainingIgnoreCase(query);
    }
}
