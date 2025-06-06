package be.ucll.campusapp.dto;

import java.time.LocalDate;

public class UserDTO {
    private Long id;
    private String voornaam;
    private String achternaam;
    private String mail;
    private LocalDate geboortedatum;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getVoornaam() { return voornaam; }
    public void setVoornaam(String voornaam) { this.voornaam = voornaam; }
    public String getAchternaam() { return achternaam; }
    public void setAchternaam(String achternaam) { this.achternaam = achternaam; }
    public String getMail() {return mail;}
    public void setMail(String mail) {
        this.mail = mail;
    }
    public LocalDate getGeboortedatum() {
        return geboortedatum;
    }
    public void setGeboortedatum(LocalDate geboortedatum) {
        this.geboortedatum = geboortedatum;
    }
}
