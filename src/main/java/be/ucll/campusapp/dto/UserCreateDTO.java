package be.ucll.campusapp.dto;

import jakarta.validation.constraints.NotBlank;

public class UserCreateDTO {
    @NotBlank(message = "Voornaam mag niet leeg zijn")
    private String voornaam;

    @NotBlank(message = "Achternaam mag niet leeg zijn")
    private String achternaam;

    // Getters en setters
    public String getVoornaam() { return voornaam; }
    public void setVoornaam(String voornaam) { this.voornaam = voornaam; }

    public String getAchternaam() { return achternaam; }
    public void setAchternaam(String achternaam) { this.achternaam = achternaam; }
}
