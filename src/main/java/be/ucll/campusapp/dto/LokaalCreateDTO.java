package be.ucll.campusapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class LokaalCreateDTO {
    @NotBlank(message="Naam mag niet leeg zijn!")
    private String naam;
    @NotBlank (message="Type mag niet leeg zijn!")
    private String type;
    @Positive(message="Het aantal personen dat in de ruimte kan, moet altijd meer dan 0 zijn.")
    private int aantalPersonen;
    @NotBlank (message="Voornaam verantwoordelijke mag niet leeg zijn!")
    private String voornaam;
    @NotBlank (message="Achternaam verantwoordelijke mag niet leeg zijn!")
    private String achternaam;
    private int verdieping;

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
}

