package be.ucll.campusapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*; // Voor JPA-annotaties zoals @Entity, @Id, @ManyToOne, enz.
import jakarta.validation.constraints.*; // Voor validatie: @NotBlank, @Positive, enz.

import java.util.HashSet;
import java.util.Set;

@Entity
public class Lokaal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank (message="Naam mag niet leeg zijn!")
    private String naam;
    @NotBlank (message="Type mag niet leeg zijn!")
    private String type;
    @Positive (message="Het aantal personen dat in de ruimte kan, moet altijd meer dan 0 zijn.")
    private int aantalPersonen;
    @NotBlank (message="Voornaam verantwoordelijke mag niet leeg zijn!")
    private String voornaam;
    @NotBlank (message="Achternaam verantwoordelijke mag niet leeg zijn!")
    private String achternaam;
    private int verdieping;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campus_naam")
    @JsonBackReference
    private Campus campus;

    public Lokaal() {}
    public Lokaal(String naam, String type, int aantalPersonen, String voornaam, String achternaam, int verdieping, Campus campus) {
        this.naam = naam;
        this.type = type;
        this.aantalPersonen = aantalPersonen;
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.verdieping = verdieping;
        this.campus = campus;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNaam() {
        return naam;
    }
    public void setNaam(String naam) {
        this.naam = naam;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getAantalPersonen() {
        return aantalPersonen;
    }
    public void setAantalPersonen(int aantalPersonen) {
        this.aantalPersonen = aantalPersonen;
    }
    public String getVoornaam() {
        return voornaam;
    }
    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }
    public String getAchternaam() {
        return achternaam;
    }
    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }
    public int getVerdieping() {
        return verdieping;
    }
    public void setVerdieping(int verdieping) {
        this.verdieping = verdieping;
    }
    public Campus getCampus() {
        return campus;
    }
    public void setCampus(Campus campus) {
        this.campus = campus;
    }
    @ManyToMany(mappedBy = "lokalen")
    private Set<Reservatie> reservaties = new HashSet<>();
    public Set<Reservatie> getReservaties() {
        return reservaties;
    }
    public void setReservaties(Set<Reservatie> reservaties) {
        this.reservaties = reservaties;
    }
}

