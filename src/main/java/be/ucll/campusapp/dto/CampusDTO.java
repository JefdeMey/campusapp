package be.ucll.campusapp.dto;

public class CampusDTO {
    private String naam;
    private String adres;
    private int aantalParkeerplaatsen;
    private int aantalLokalen;

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
    public int getAantalParkeerplaatsen() {
        return aantalParkeerplaatsen;
    }
    public void setAantalParkeerplaatsen(int aantalParkeerplaatsen) {
        this.aantalParkeerplaatsen = aantalParkeerplaatsen;
    }
    public int getAantalLokalen() {
        return aantalLokalen;
    }
    public void setAantalLokalen(int aantalLokalen) {
        this.aantalLokalen = aantalLokalen;
    }
}

