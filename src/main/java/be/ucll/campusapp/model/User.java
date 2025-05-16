package be.ucll.campusapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Voornaam mag niet leeg zijn")
    private String voornaam;

    @NotBlank(message = "Achternaam mag niet leeg zijn")
    private String achternaam;

    // Je kunt ook loginnaam/email toevoegen als je dat wenst

    public User() {
    }

    public User(String voornaam, String achternaam) {
        this.voornaam = voornaam;
        this.achternaam = achternaam;
    }

    // Getters & setters

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
}

