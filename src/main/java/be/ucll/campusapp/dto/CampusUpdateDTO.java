package be.ucll.campusapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class CampusUpdateDTO {

    @NotBlank(message = "Adres mag niet leeg zijn")
    private String adres;

    @PositiveOrZero(message = "Aantal parkeerplaatsen mag niet negatief zijn")
    private int aantalParkeerplaatsen;

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public int getAantalParkeerplaatsen() {
        return aantalParkeerplaatsen;
    }

    public void setAantalParkeerplaatsen(int aantalParkeerplaatsen) {
        this.aantalParkeerplaatsen = aantalParkeerplaatsen;
    }
}

