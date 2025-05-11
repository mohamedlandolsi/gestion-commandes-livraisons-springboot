package itbs.mohamedlandolsi.gestioncommandeslivraisons.dto;

import itbs.mohamedlandolsi.gestioncommandeslivraisons.model.Livraison.StatutLivraison;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivraisonRequestDTO {

    @NotNull(message = "L'ID de la commande ne peut pas être nul.")
    private Long commandeId;

    private Long transporteurId; // Optional

    @NotNull(message = "La date de livraison ne peut pas être nulle.")
    private LocalDateTime dateLivraison;

    @NotBlank(message = "L'adresse de livraison ne peut pas être vide.")
    private String adresseLivraison;

    private BigDecimal cout; // Optional

    private StatutLivraison statut; // Optional, defaults to EN_ATTENTE in Entity
}
