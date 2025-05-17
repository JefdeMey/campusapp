package be.ucll.campusapp.dto;

public class UserDTO {
    private Long id;
    private String voornaam;
    private String achternaam;

    // Getters en setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getVoornaam() { return voornaam; }
    public void setVoornaam(String voornaam) { this.voornaam = voornaam; }

    public String getAchternaam() { return achternaam; }
    public void setAchternaam(String achternaam) { this.achternaam = achternaam; }
}
