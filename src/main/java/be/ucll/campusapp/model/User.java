package be.ucll.campusapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

@Entity
@Table(name="app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    public User() {}
    public User(String voornaam, String achternaam, String mail, LocalDate geboortedatum) {
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.mail = mail;
        this.geboortedatum = geboortedatum;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
    public LocalDate getGeboortedatum() { return geboortedatum; }
    public void setGeboortedatum(LocalDate geboortedatum) { this.geboortedatum = geboortedatum; }
}

