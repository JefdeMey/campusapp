package be.ucll.campusapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public class UserCreateDTO {
    @NotBlank(message = "Voornaam mag niet leeg zijn")
    private String voornaam;

    @NotBlank(message = "Achternaam mag niet leeg zijn")
    private String achternaam;
    @NotBlank(message = "E-mailadres is verplicht.")
    @Email(message = "Ongeldig e-mailadres.")
    private String mail;

    @NotNull(message = "Geboortedatum is verplicht.")
    @Past(message = "Geboortedatum moet in het verleden liggen.")
    private LocalDate geboortedatum;

    // Getters en setters
    public String getVoornaam() { return voornaam; }
    public void setVoornaam(String voornaam) { this.voornaam = voornaam; }

    public String getAchternaam() { return achternaam; }
    public void setAchternaam(String achternaam) { this.achternaam = achternaam; }

    public String getMail() { return mail; }

    public void setMail(String mail) { this.mail = mail; }

    public LocalDate getGeboortedatum() { return geboortedatum; }

    public void setGeboortedatum(LocalDate geboortedatum) { this.geboortedatum = geboortedatum; }
}
