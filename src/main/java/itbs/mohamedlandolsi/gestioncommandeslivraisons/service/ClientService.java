package itbs.mohamedlandolsi.gestioncommandeslivraisons.service;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Client;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return clientRepository.existsById(id);
    }

    public List<Client> searchClientsByName(String query) {
        return clientRepository.findByNomContainingIgnoreCase(query);
    }

    public List<Client> getClientsByCity(String city) {
        // Assuming client address contains city information
        return clientRepository.findByAdresseContainingIgnoreCase(city);
    }

    public List<Client> getClientsByPostalCode(String postalCode) {
        // Assuming client address contains postal code
        return clientRepository.findByAdresseContainingIgnoreCase(postalCode);
    }

    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmailIgnoreCase(email);
    }
}