package itbs.mohamedlandolsi.gestioncommandeslivraisons.model;

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
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "transporteur_id")
    private Transporteur transporteur;

    private LocalDateTime dateLivraison;

    private BigDecimal cout;

    @Enumerated(EnumType.STRING)
    private StatutLivraison statut = StatutLivraison.EN_ATTENTE;

    public enum StatutLivraison {
        EN_ATTENTE, EN_COURS, LIVREE, RETARDEE, ANNULEE
    }
}