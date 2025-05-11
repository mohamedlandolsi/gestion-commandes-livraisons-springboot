package itbs.mohamedlandolsi.gestioncommandeslivraisons.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transporteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    private String telephone;

    private Double note;

    @OneToMany(mappedBy = "transporteur")
    @JsonManagedReference("transporteur-livraisons")
    private List<Livraison> livraisons;
}