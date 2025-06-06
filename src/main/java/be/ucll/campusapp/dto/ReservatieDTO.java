package be.ucll.campusapp.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ReservatieDTO {

    private Long id;
    private LocalDateTime startTijd;
    private LocalDateTime eindTijd;
    private String commentaar;
    private int aantalPersonen;
    private String gebruikerNaam;
    private List<String> lokaalNamen;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getStartTijd() { return startTijd; }
    public void setStartTijd(LocalDateTime startTijd) { this.startTijd = startTijd; }
    public LocalDateTime getEindTijd() { return eindTijd; }
    public void setEindTijd(LocalDateTime eindTijd) { this.eindTijd = eindTijd; }
    public String getCommentaar() { return commentaar; }
    public void setCommentaar(String commentaar) { this.commentaar = commentaar; }
    public int getAantalPersonen() { return aantalPersonen; }
    public void setAantalPersonen(int aantalPersonen) { this.aantalPersonen = aantalPersonen; }
    public String getGebruikerNaam() { return gebruikerNaam; }
    public void setGebruikerNaam(String gebruikerNaam) { this.gebruikerNaam = gebruikerNaam; }
    public List<String> getLokaalNamen() { return lokaalNamen; }
    public void setLokaalNamen(List<String> lokaalNamen) { this.lokaalNamen = lokaalNamen; }
}
