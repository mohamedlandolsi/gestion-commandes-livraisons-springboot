package itbs.mohamedlandolsi.gestioncommandeslivraisons.service;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.exception.CommandeValidationException;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Client;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Commande;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.LigneCommande;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Commande.StatutCommande;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.repository.ClientRepository;
import itbs.mohamedlandolsi.gestioncommandeslivraisons.repository.CommandeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final ClientRepository clientRepository;
    private final ProduitService produitService;
    private final LigneCommandeService ligneCommandeService;

    @Autowired
    public CommandeService(
            CommandeRepository commandeRepository, 
            ClientRepository clientRepository,
            ProduitService produitService,
            LigneCommandeService ligneCommandeService) {
        this.commandeRepository = commandeRepository;
        this.clientRepository = clientRepository;
        this.produitService = produitService;
        this.ligneCommandeService = ligneCommandeService;
    }

    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    public Optional<Commande> getCommandeById(Long id) {
        return commandeRepository.findById(id);
    }

    /**
     * Creates a new order with comprehensive validation workflow
     * @param commande the order to validate and save
     * @return saved order
     * @throws CommandeValidationException if validation fails
     * @throws IllegalStateException if stock is insufficient
     */
    @Transactional
    public Commande saveCommande(Commande commande) {
        validateFullOrder(commande);
        
        // Set initial status if not already set
        if (commande.getStatut() == null) {
            commande.setStatut(StatutCommande.EN_ATTENTE);
        }
        
        // Set date if not already set
        if (commande.getDate() == null) {
            commande.setDate(LocalDateTime.now());
        }
        
        // Calculate the total amount
        if (commande.getLignesCommande() != null && !commande.getLignesCommande().isEmpty()) {
            BigDecimal total = calculateOrderTotal(commande.getLignesCommande());
            commande.setMontantTotal(total);
        }
        
        // Save the order
        return commandeRepository.save(commande);
    }
    
    /**
     * Comprehensive validation of an order
     * @param commande the order to validate
     * @throws CommandeValidationException if validation fails
     */
    private void validateFullOrder(Commande commande) {
        // Validate client
        validateClient(commande);
        
        // Validate order lines
        validateOrderLines(commande);
        
        // Check stock availability
        if (commande.getLignesCommande() != null && !commande.getLignesCommande().isEmpty()) {
            validateStockForOrder(commande.getLignesCommande());
        }
        
        // Validate minimum order amount
        if (commande.getLignesCommande() != null && !commande.getLignesCommande().isEmpty()) {
            BigDecimal total = calculateOrderTotal(commande.getLignesCommande());
            if (total.compareTo(BigDecimal.ZERO) <= 0) {
                throw new CommandeValidationException("The order total must be greater than zero");
            }
        }
    }
    
    /**
     * Validates the client in an order
     * @param commande the order to validate
     * @throws CommandeValidationException if client validation fails
     */
    private void validateClient(Commande commande) {
        if (commande.getClient() == null || commande.getClient().getId() == null) {
            throw new CommandeValidationException("Client information is required");
        }
        
        Long clientId = commande.getClient().getId();
        if (!clientRepository.existsById(clientId)) {
            throw new CommandeValidationException("Client with ID " + clientId + " does not exist");
        }
        
        // Ensure we have the complete client entity
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new CommandeValidationException("Client with ID " + clientId + " not found"));
        commande.setClient(client);
    }
    
    /**
     * Validates the order lines in an order
     * @param commande the order to validate
     * @throws CommandeValidationException if order line validation fails
     */
    private void validateOrderLines(Commande commande) {
        if (commande.getLignesCommande() == null || commande.getLignesCommande().isEmpty()) {
            throw new CommandeValidationException("Order must contain at least one item");
        }
        
        for (LigneCommande ligne : commande.getLignesCommande()) {
            // Validate product exists
            if (ligne.getProduit() == null || ligne.getProduit().getId() == null) {
                throw new CommandeValidationException("Product information is missing in order line");
            }
            
            // Validate quantity
            if (ligne.getQuantite() == null || ligne.getQuantite() <= 0) {
                throw new CommandeValidationException("Product quantity must be greater than zero");
            }
            
            // Validate price
            if (ligne.getPrixUnitaire() == null || ligne.getPrixUnitaire().compareTo(BigDecimal.ZERO) <= 0) {
                throw new CommandeValidationException("Product price must be greater than zero");
            }
            
            // Set reference to parent order
            ligne.setCommande(commande);
        }
    }

    /**
     * Validates that there is enough stock for all products in the order
     * @param lignesCommande the order items to validate
     * @throws CommandeValidationException if stock is insufficient
     */
    private void validateStockForOrder(List<LigneCommande> lignesCommande) {
        Map<Long, Integer> productQuantities = new HashMap<>();
        
        // Aggregate quantities by product
        for (LigneCommande ligne : lignesCommande) {
            if (ligne.getProduit() == null || ligne.getProduit().getId() == null) {
                throw new CommandeValidationException("Product information is missing");
            }
            
            Long produitId = ligne.getProduit().getId();
            productQuantities.put(produitId, 
                    productQuantities.getOrDefault(produitId, 0) + ligne.getQuantite());
        }
        
        // Check stock availability for each product
        List<String> insufficientStockProducts = new ArrayList<>();
        
        for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
            Long produitId = entry.getKey();
            Integer totalQuantity = entry.getValue();
            
            if (!produitService.hasEnoughStock(produitId, totalQuantity)) {
                String productName = produitService.getProduitById(produitId)
                    .map(p -> p.getNom())
                    .orElse("ID: " + produitId);
                insufficientStockProducts.add(productName);
            }
        }
        
        if (!insufficientStockProducts.isEmpty()) {
            String productList = String.join(", ", insufficientStockProducts);
            throw new CommandeValidationException("Insufficient stock for products: " + productList);
        }
    }
    
    /**
     * Calculates the total amount of the order
     * @param lignesCommande the order items
     * @return total amount
     */
    private BigDecimal calculateOrderTotal(List<LigneCommande> lignesCommande) {
        return lignesCommande.stream()
                .map(ligne -> ligne.getPrixUnitaire().multiply(new BigDecimal(ligne.getQuantite())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void deleteCommande(Long id) {
        commandeRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return commandeRepository.existsById(id);
    }

    /**
     * Updates the status of an order with validation of allowed transitions
     * @param id order ID
     * @param statut new status
     * @return updated order
     * @throws CommandeValidationException if status transition is not allowed
     */
    @Transactional
    public Commande updateCommandeStatus(Long id, StatutCommande statut) {
        Optional<Commande> commandeOpt = commandeRepository.findById(id);
        if (commandeOpt.isEmpty()) {
            throw new CommandeValidationException("Order with ID " + id + " not found");
        }
        
        Commande commande = commandeOpt.get();
        StatutCommande oldStatus = commande.getStatut();
        
        // Validate status transition
        validateStatusTransition(oldStatus, statut);
        
        // Process special status change logic
        processStatusChange(commande, oldStatus, statut);
        
        return commandeRepository.save(commande);
    }
    
    /**
     * Process logic for specific status changes
     * @param commande the order being updated
     * @param oldStatus the previous status
     * @param newStatus the new status
     */
    private void processStatusChange(Commande commande, StatutCommande oldStatus, StatutCommande newStatus) {
        commande.setStatut(newStatus);
        
        switch (newStatus) {
            case VALIDEE:
                // When an order is validated, we might want to check payments, etc.
                validatePaymentForOrder(commande);
                break;
            case ANNULEE:
                // When an order is canceled, we might need to release reserved inventory
                handleOrderCancellation(commande);
                break;
            case EN_PREPARATION:
                // Additional logic for when an order enters preparation
                break;
            case EXPEDIEE:
                // Update shipping information
                break;
            case LIVREE:
                // Mark delivery as completed
                break;
            default:
                // Default handling
                break;
        }
    }
    
    /**
     * Validates that payment is present for an order being validated
     * @param commande the order to check
     */
    private void validatePaymentForOrder(Commande commande) {
        // Implement payment validation logic
        // This is a placeholder for actual payment validation
    }
    
    /**
     * Handles necessary operations when an order is canceled
     * @param commande the canceled order
     */
    private void handleOrderCancellation(Commande commande) {
        // Implement cancellation logic
        // This is a placeholder for actual cancellation handling
    }
    
    /**
     * Validates if a status transition is allowed
     * @param oldStatus current status
     * @param newStatus new status
     * @throws CommandeValidationException if transition is not allowed
     */
    private void validateStatusTransition(StatutCommande oldStatus, StatutCommande newStatus) {
        // Define allowed transitions using a map for clarity and maintainability
        Map<StatutCommande, Set<StatutCommande>> allowedTransitions = new HashMap<>();
        
        allowedTransitions.put(StatutCommande.EN_ATTENTE, 
                new HashSet<>(Arrays.asList(StatutCommande.VALIDEE, StatutCommande.ANNULEE)));
        
        allowedTransitions.put(StatutCommande.VALIDEE, 
                new HashSet<>(Arrays.asList(StatutCommande.EN_PREPARATION, StatutCommande.ANNULEE)));
        
        allowedTransitions.put(StatutCommande.EN_PREPARATION, 
                new HashSet<>(Arrays.asList(StatutCommande.EXPEDIEE, StatutCommande.ANNULEE)));
        
        allowedTransitions.put(StatutCommande.EXPEDIEE, 
                new HashSet<>(Arrays.asList(StatutCommande.LIVREE)));
        
        // LIVREE and ANNULEE are final states
        allowedTransitions.put(StatutCommande.LIVREE, Collections.emptySet());
        allowedTransitions.put(StatutCommande.ANNULEE, Collections.emptySet());
        
        // Check if transition is allowed
        if (!allowedTransitions.containsKey(oldStatus) || 
                !allowedTransitions.get(oldStatus).contains(newStatus)) {
            throw new CommandeValidationException(
                    "Status transition from " + oldStatus + " to " + newStatus + " is not allowed");
        }
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