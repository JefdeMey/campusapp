package be.ucll.campusapp.dto;

public class LokaalDTO {
    private Long id;
    private String naam;
    private String type;
    private int aantalPersonen;
    private String voornaam;
    private String achternaam;
    private int verdieping;
    private String campusNaam;

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
    public String getCampusNaam() {
        return campusNaam;
    }
    public void setCampusNaam(String campusNaam) {
        this.campusNaam = campusNaam;
    }
}

