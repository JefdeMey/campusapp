package be.ucll.campusapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*; // Voor JPA-annotaties zoals @Entity, @Id, @ManyToOne, enz.
import jakarta.validation.constraints.*; // Voor validatie: @NotBlank, @Positive, enz.

@Entity // Deze klasse is een JPA-entiteit, en wordt een tabel in de database
public class Lokaal {

    @Id // Primaire sleutel
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Laat de database automatisch een uniek ID genereren
    private Long id;

    @NotBlank (message="Naam mag niet leeg zijn!") // Validatie: mag niet leeg of null zijn
    private String naam;

    @NotBlank (message="Type mag niet leeg zijn!")
    private String type;

    @Positive (message="Het aantal personen dat in de ruimte kan, moet altijd meer dan 0 zijn.") // Mag 0 zijn, of positief (bv. computerlokaal zonder stoelen is OK)
    private int aantalPersonen;

    @NotBlank (message="Voornaam verantwoordelijke mag niet leeg zijn!")
    private String voornaam;

    @NotBlank (message="Achternaam verantwoordelijke mag niet leeg zijn!")
    private String achternaam;

    private int verdieping; // Geen validatie: mag ook negatief zijn (bv. -1 = kelder)

    @ManyToOne // Veel lokalen kunnen bij één campus horen
    @JoinColumn(name = "campus_naam") // Foreign key in de tabel die verwijst naar Campus.naam , omdat dit PK is

    //@JsonIgnoreProperties("lokalen")
    @JsonBackReference
    private Campus campus;

    // DEFAULT CONSTRUCTOR
    public Lokaal() {
    }

    // CONSTRUCTOR met parameters (optioneel, handig bij testen of builder-pattern)
    public Lokaal(String naam, String type, int aantalPersonen, String voornaam, String achternaam, int verdieping, Campus campus) {
        this.naam = naam;
        this.type = type;
        this.aantalPersonen = aantalPersonen;
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.verdieping = verdieping;
        this.campus = campus;
    }

    // GETTERS & SETTERS

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
}

