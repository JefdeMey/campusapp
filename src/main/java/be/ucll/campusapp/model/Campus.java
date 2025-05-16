package be.ucll.campusapp.model;
//IMPORTS
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*; // Importeert JPA-annotaties zoals @Entity, @Id, @OneToMany, @Transient, enz.
import jakarta.validation.constraints.NotBlank; // Zorgt ervoor dat een String niet null of leeg mag zijn
import jakarta.validation.constraints.Positive; // Zorgt ervoor dat een numeriek veld positief moet zijn
import jakarta.validation.constraints.PositiveOrZero;

import java.util.ArrayList;
import java.util.List;

// ENTITY DEFINITIE

@Entity // Duidt aan dat deze klasse een JPA-entiteit is (en dus een tabel zal worden in de database)
public class Campus {

    @Id // Duidt aan dat 'naam' de primaire sleutel is (unieke identificatie in de database)
    @NotBlank(message="Naam mag niet leeg zijn!")
    private String naam;

    @NotBlank(message="Adres mag niet leeg zijn!")
    private String adres;

    @PositiveOrZero(message="aantal parkeerplaatsen kan niet negatief zijn!")
    private int aantalParkeerplaatsen;

    @OneToMany( // Een campus heeft meerdere lokalen (1-n relatie)
            mappedBy = "campus", // 'campus' is het veld in de Lokaal-klasse die de relatie teruglegt (foreign key)
            cascade = CascadeType.ALL, // Wijzigingen aan een campus worden ook toegepast op de bijbehorende lokalen
            orphanRemoval = true // Als een lokaal uit de lijst wordt verwijderd, wordt het ook verwijderd uit de database
    )
    @JsonManagedReference // zorgt ervoor dat deze kant als "de hoofdstructuur" wordt gezien
    private List<Lokaal> lokalen = new ArrayList<>(); // Lijst van lokalen die bij deze campus horen

    // DEFAULT CONSTRUCTOR (vereist door JPA)
    public Campus() {
    }

    // CONSTRUCTOR MET PARAMETERS
    public Campus(String naam, String adres, int aantalParkeerplaatsen) {
        this.naam = naam;
        this.adres = adres;
        this.aantalParkeerplaatsen = aantalParkeerplaatsen;
    }

    // GETTERS EN SETTERS

    public String getNaam() { return naam; }

    public void setNaam(String naam) { this.naam = naam; }

    public String getAdres() { return adres; }

    public void setAdres(String adres) { this.adres = adres; }

    public int getAantalParkeerplaatsen() { return aantalParkeerplaatsen; }

    public void setAantalParkeerplaatsen(int aantalParkeerplaatsen) { this.aantalParkeerplaatsen = aantalParkeerplaatsen;  }

    public List<Lokaal> getLokalen() { return lokalen; }

    public void setLokalen(List<Lokaal> lokalen) { this.lokalen = lokalen; }

    @Transient // Wordt niet in de database opgeslagen, maar enkel berekend in het object zelf
    public int getAantalLokalen() {
        return lokalen.size(); // Geeft het aantal lokalen terug (wordt niet opgeslagen als kolom)
    }
}
