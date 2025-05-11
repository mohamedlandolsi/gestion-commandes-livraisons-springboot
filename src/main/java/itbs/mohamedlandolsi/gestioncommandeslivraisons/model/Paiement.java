package itbs.mohamedlandolsi.gestioncommandeslivraisons.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "commande_id", nullable = false)
    @JsonManagedReference("commande-paiement")
    private Commande commande;

    private LocalDateTime date = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private StatutPaiement statut = StatutPaiement.EN_ATTENTE;

    @Enumerated(EnumType.STRING)
    private ModePaiement mode;

    private Double montantPaye;

    public enum StatutPaiement {
        EN_ATTENTE, EFFECTUE, ECHEC, REMBOURSE
    }

    public enum ModePaiement {
        CARTE_CREDIT, VIREMENT, PAYPAL, ESPECES, CHEQUE
    }
}