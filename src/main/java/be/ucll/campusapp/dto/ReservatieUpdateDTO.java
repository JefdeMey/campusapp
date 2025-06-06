package be.ucll.campusapp.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class ReservatieUpdateDTO {

    @NotNull(message = "Starttijd is verplicht.")
    @Future(message = "Starttijd moet in de toekomst liggen.")
    private LocalDateTime startTijd;
    @NotNull(message = "Eindtijd is verplicht.")
    @Future(message = "Eindtijd moet in de toekomst liggen.")
    private LocalDateTime eindTijd;
    private String commentaar;
    @Min(value = 1, message = "Aantal personen moet minstens 1 zijn.")
    private int aantalPersonen;
    @NotNull(message = "Lijst van lokaal-IDs is verplicht.")
    @NotEmpty(message="Er moet minstens 1 lokaal gekozen worden.")
    private List<Long> lokaalIds;

    public LocalDateTime getStartTijd() { return startTijd; }
    public void setStartTijd(LocalDateTime startTijd) { this.startTijd = startTijd; }
    public LocalDateTime getEindTijd() { return eindTijd; }
    public void setEindTijd(LocalDateTime eindTijd) { this.eindTijd = eindTijd; }
    public String getCommentaar() { return commentaar; }
    public void setCommentaar(String commentaar) { this.commentaar = commentaar; }
    public int getAantalPersonen() { return aantalPersonen; }
    public void setAantalPersonen(int aantalPersonen) { this.aantalPersonen = aantalPersonen; }
    public List<Long> getLokaalIds() { return lokaalIds; }
    public void setLokaalIds(List<Long> lokaalIds) { this.lokaalIds = lokaalIds; }
}
