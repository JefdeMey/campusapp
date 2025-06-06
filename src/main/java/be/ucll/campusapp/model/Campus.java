package be.ucll.campusapp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.PositiveOrZero;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Campus {
    @Id
    @NotBlank(message="Naam mag niet leeg zijn!")
    private String naam;
    @NotBlank(message="Adres mag niet leeg zijn!")
    private String adres;
    @PositiveOrZero(message="aantal parkeerplaatsen kan niet negatief zijn!")
    private int aantalParkeerplaatsen;
    @OneToMany(
            mappedBy = "campus",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<Lokaal> lokalen = new ArrayList<>();

    public Campus() {
    }
    public Campus(String naam, String adres, int aantalParkeerplaatsen) {
        this.naam = naam;
        this.adres = adres;
        this.aantalParkeerplaatsen = aantalParkeerplaatsen;
    }

    public String getNaam() { return naam; }
    public void setNaam(String naam) { this.naam = naam; }
    public String getAdres() { return adres; }
    public void setAdres(String adres) { this.adres = adres; }
    public int getAantalParkeerplaatsen() { return aantalParkeerplaatsen; }
    public void setAantalParkeerplaatsen(int aantalParkeerplaatsen) { this.aantalParkeerplaatsen = aantalParkeerplaatsen;  }
    public List<Lokaal> getLokalen() { return lokalen; }
    public void setLokalen(List<Lokaal> lokalen) { this.lokalen = lokalen; }
    @Transient
    public int getAantalLokalen() { return lokalen.size(); }
}
