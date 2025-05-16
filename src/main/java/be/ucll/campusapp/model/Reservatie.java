package be.ucll.campusapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Reservatie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User gebruiker;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lokaal_id")
    private Lokaal lokaal;

    @NotNull(message = "Starttijd is verplicht")
    @Future(message = "Starttijd moet in de toekomst liggen")
    private LocalDateTime startTijd;

    @NotNull(message = "Eindtijd is verplicht")
    @Future(message = "Eindtijd moet in de toekomst liggen")
    private LocalDateTime eindTijd;

    public Reservatie() {
    }

    public Reservatie(User gebruiker, Lokaal lokaal, LocalDateTime startTijd, LocalDateTime eindTijd) {
        this.gebruiker = gebruiker;
        this.lokaal = lokaal;
        this.startTijd = startTijd;
        this.eindTijd = eindTijd;
    }

    // Getters & setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getGebruiker() {
        return gebruiker;
    }

    public void setGebruiker(User gebruiker) {
        this.gebruiker = gebruiker;
    }

    public Lokaal getLokaal() {
        return lokaal;
    }

    public void setLokaal(Lokaal lokaal) {
        this.lokaal = lokaal;
    }

    public LocalDateTime getStartTijd() {
        return startTijd;
    }

    public void setStartTijd(LocalDateTime startTijd) {
        this.startTijd = startTijd;
    }

    public LocalDateTime getEindTijd() {
        return eindTijd;
    }

    public void setEindTijd(LocalDateTime eindTijd) {
        this.eindTijd = eindTijd;
    }
}

