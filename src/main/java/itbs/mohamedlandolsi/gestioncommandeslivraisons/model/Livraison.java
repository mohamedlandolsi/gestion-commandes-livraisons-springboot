package itbs.mohamedlandolsi.gestioncommandeslivraisons.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livraison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "commande_id", nullable = false)
    @JsonManagedReference("livraison-commande")
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "transporteur_id")
    private Transporteur transporteur;

    private LocalDateTime dateLivraison;

    private String adresseLivraison; // Added this field

    private BigDecimal cout;

    @Enumerated(EnumType.STRING)
    private StatutLivraison statut = StatutLivraison.EN_ATTENTE;

    public enum StatutLivraison {
        EN_ATTENTE, EN_COURS, LIVREE, RETARDEE, ANNULEE
    }
}