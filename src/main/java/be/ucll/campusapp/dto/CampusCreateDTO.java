package be.ucll.campusapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CampusCreateDTO {
    @NotBlank(message = "Naam is verplicht.")
    private String naam;
    @NotBlank(message = "Adres is verplicht.")
    private String adres;
    @Min(value = 1, message = "aantal parkeerplaatsen moet minstens 1 zijn.")
    private int aantalParkeerPlaatsen;

    // Getters en setters

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }


    public int getAantalParkeerPlaatsen() {
        return aantalParkeerPlaatsen;
    }

    public void setAantalParkeerPlaatsen(int aantalParkeerPlaatsen) {
        this.aantalParkeerPlaatsen = aantalParkeerPlaatsen;
    }
}

